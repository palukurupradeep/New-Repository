# Stage 1: Compile and Build Angular codebase
# Use Node.js 20.11.0 image as the base image 
FROM node:20.11.0 as build

# Set the working directory
WORKDIR /usr/local/app

# Add the source code to the working directory
COPY ./ /usr/local/app/

# Configure npm to ignore SSL certificate errors
RUN npm config set strict-ssl false

# Install all the dependencies
RUN npm install --legacy-peer-deps

# Generate the build of the application
RUN npm run build:sys --output-hashing=all

# Stage 2: Serve app with nginx server
# Use the official nginx image as the base image
FROM nginx:latest

# Copy the build output to replace the default nginx contents
COPY --from=build /usr/local/app/dist/shaw-claim-mang-v1 /usr/share/nginx/html

# Copy the custom nginx configuration file
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Expose port 80
EXPOSE 80
EXPOSE 4200