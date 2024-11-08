# fs-claims
Claims3d


# Claim Deployment Pipeline: CI/CD Pipeline Setup Guide

This guide provides step-by-step instructions for setting up a Continuous Integration and Continuous Deployment (CI/CD) pipeline for the Claim Services project. The pipeline automates Docker image builds, pushes to Azure Container Registry (ACR), and deployment to Azure Kubernetes Service (AKS). It is intended for production environments replicating a development-to-production workflow.

## Prerequisites

Ensure you have the following components ready before starting the setup:

### 1. **Infrastructure Prerequisites**
   - **Azure Kubernetes Service (AKS)**: A running AKS cluster for deployment.
   - **Azure Container Registry (ACR)**: ACR must be configured to store Docker images.
   - **Azure Service Principal**: Used for authentication with Azure services.
   - **GitHub Repository**: Ensure your application code is stored in a GitHub repository.

### 2. **Secrets and Credentials**
   The following secrets must be configured in your GitHub repository. They are required for interacting with Azure services:

   - `AZURE_CLIENT_ID`: Service principal client ID.
   - `AZURE_CLIENT_SECRET`: Service principal client secret.
   - `AZURE_TENANT_ID`: Azure tenant ID.
   - `AZURE_SUBSCRIPTION_ID`: Azure subscription ID.
   - `AKS_RESOURCE_GROUP`: Resource group name for the AKS cluster.
   - `AKS_CLUSTER_NAME`: Name of your AKS cluster.

## Pipeline Overview

The pipeline is divided into two stages:
1. **Build Stage**: Builds Docker images for each service and pushes them to ACR.
2. **Deploy Stage**: Deploys the services from ACR to AKS.

This pipeline is triggered by a push to the `SYS_Deployment_Branch`.

---

## Pipeline Configuration

Below is the GitHub Actions pipeline configuration. This file should be placed in `.github/workflows/claim-deployment.yaml` in the `fs-claims` repository.

```yaml
name: Claim Deployment Pipeline
on:
  push:
    branches:
      - SYS_Deployment_Branch

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
    - name: Checkout Repository
      uses: actions/checkout@v2
      # Significance: Checks out your code so subsequent steps can access it.

    - name: Set up JDK 17
      uses: actions/setup-java@v2
      with:
        distribution: 'adopt'
        java-version: '17'
      # Significance: Ensures the correct Java version is installed, crucial for building Java-based services.

    - name: Set up Docker Build
      uses: docker/setup-buildx-action@v1
      # Significance: Enables Docker's Buildx, allowing multi-platform builds for more complex images.

    - name: Set up kubectl
      uses: azure/setup-kubectl@v1
      with:
        kubeconfig: ${{ github.workspace }}/kubeconfig
      # Significance: Installs kubectl to allow interactions with the Kubernetes cluster.

    - uses: azure/use-kubelogin@v1
      with:
        kubelogin-version: 'v0.0.24'
      # Significance: Enables Azure-based login for kubectl, crucial for secure interaction with AKS.

    - name: Compiling the Docker Images
      run: |
        docker build -t claim-initiation-sys-service:latest claim-initiation-service/
        docker build -t claim-integration-sys-service:latest claim-integration-service/
        docker build -t claim-security-sys-service:latest claim-security-service/
        docker build -t claim-ui-sys-service:latest claim-ui/
        # Significance: Builds Docker images for each service in the repository.

        az login --service-principal -u ${{ secrets.AZURE_CLIENT_ID }} -p ${{ secrets.AZURE_CLIENT_SECRET }} --tenant ${{ secrets.AZURE_TENANT_ID }}
        az acr login --name szue2fintechaksdacr1
        # Significance: Logs in to Azure and authenticates with the Azure Container Registry.

        docker tag claim-initiation-sys-service:latest szue2fintechaksdacr1.azurecr.io/claim-initiation-sys-service:latest
        docker push szue2fintechaksdacr1.azurecr.io/claim-initiation-sys-service:latest
        # Significance: Tags and pushes the Docker image to ACR.

        # Repeats for other services...

```

##  Build Stage Explanation
Caveats:
    - **Java Version**: Ensure the application is compatible with JDK 17. Use the appropriate distribution (adopt) in the configuration.
    - **Docker Builds**: Make sure Dockerfiles are correctly configured for each service.
    - **Azure Authentication**: You need to ensure the service principal has the correct permissions to access ACR and AKS.


## Deployment Configuration

```yaml
  deploy:
    needs: build
    runs-on: [self-hosted, linux]
    # Alternative: runs-on: ubuntu-latest, but using self-hosted gives more control and security for production deployments.

    steps:
    - name: Checkout Repository
      uses: actions/checkout@v2
      # Significance: Ensures the deployment files are available for the deployment step.

    - name: Set up JDK 17
      uses: actions/setup-java@v2
      with:
        distribution: 'adopt'
        java-version: '17'
      # Significance: Ensures compatibility with Java-based services if any scripts require it during deployment.

    - name: Set up kubectl
      uses: azure/setup-kubectl@v1
      with:
        kubeconfig: ${{ github.workspace }}/kubeconfig
      # Significance: Installs kubectl and configures it to access the AKS cluster.

    - uses: azure/use-kubelogin@v1
      with:
        kubelogin-version: 'v0.0.24'
      # Significance: Ensures that kubelogin is configured to work with Azure Active Directory for AKS authentication.

    - name: Deployment to AKS
      run: |
        az login --service-principal -u ${{ secrets.AZURE_CLIENT_ID }} -p ${{ secrets.AZURE_CLIENT_SECRET }} --tenant ${{ secrets.AZURE_TENANT_ID }}
        az account set --subscription ${{ secrets.AZURE_SUBSCRIPTION_ID }}
        az aks get-credentials --resource-group ${{ secrets.AKS_RESOURCE_GROUP }} --name ${{ secrets.AKS_CLUSTER_NAME }} --overwrite-existing
        # Significance: Logs into Azure, sets the appropriate subscription, and fetches the AKS cluster credentials.

        kubelogin convert-kubeconfig -l azurecli
        az acr login --name szue2fintechaksdacr1 --expose-token
        # Significance: Ensures kubelogin works seamlessly with the kubeconfig for AKS.

        NAMESPACE="sys"
        if ! kubectl get namespace "${NAMESPACE}" &> /dev/null; then
            kubectl create namespace "${NAMESPACE}"
        fi
        # Significance: Ensures the target namespace exists in the cluster; if not, it creates it.

        kubectl apply -f k8s/storybook/claim-initiation-service/deployment.yaml -f k8s/storybook/claim-initiation-service/ingress.yaml -f k8s/storybook/claim-initiation-service/service.yaml
        # Significance: Deploys the Claim Initiation Service to AKS.

        # Repeats for other services...
        
        # Scaling strategy
        kubectl scale deployments claim-ui-sys-service-deployment -n ${NAMESPACE} --replicas 0
        kubectl scale deployments claim-ui-sys-service-deployment -n ${NAMESPACE} --replicas 1
        # Significance: Scales the deployments for a fresh start in the AKS cluster.
```

##  Deployment Stage Explanation
Caveats:
    - **Namespace Creation**: Ensure the target namespace (sys in this case) is correctly created. The script handles it automatically, but make sure permissions are set up in your cluster to allow namespace creation.
    - **Kubernetes Manifests**: Verify that all manifest files (deployment, ingress, service YAML files) are properly configured for production environments.
    - **Scaling Deployments**: The scaling strategy ensures a clean restart of deployments. Depending on your application, you might need to adjust the scaling logic based on availability needs.

##  Common Caveats and Troubleshooting
        1. Authentication Failures
            Azure Login: Ensure that the service principal credentials (client ID, secret, tenant ID) are valid and that the service principal has the necessary permissions in the subscription.
            ACR Login: If the ACR login fails, ensure that the service principal has the AcrPush role assigned in the registry.
        2. Docker Build Failures
            Dockerfiles: Ensure each service has a valid and correctly configured Dockerfile. Build failures often stem from incorrect paths or missing dependencies.
        3. Kubernetes Deployment Issues
            Namespace Not Found: If the specified namespace does not exist, the deployment will fail. The script attempts to create the namespace, but ensure permissions are set correctly.
            Manifest Errors: Incorrectly configured Kubernetes manifests (e.g., missing fields, misconfigured resource limits) can cause deployment failures. Validate manifests using kubectl apply --dry-run=client.

##  How to Monitor the Pipeline
        Once the pipeline is set up and running, you can monitor it through the Actions tab in your GitHub repository. The logs will provide insights into each step, allowing you to track the success or failure of builds and deployments.

