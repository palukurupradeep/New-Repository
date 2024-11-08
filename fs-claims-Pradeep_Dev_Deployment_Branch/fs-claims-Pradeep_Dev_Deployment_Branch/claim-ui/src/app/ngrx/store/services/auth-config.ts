export const msalConfig = {
    auth: {
        clientId: '27efd66c-7944-4e2d-b7da-72e5dd3582eb',
        authority: 'https://login.microsoftonline.com/9407f998-df4c-49e1-bb48-b181e9e3f8dc', // Replace YOUR_ACTUAL_TENANT_ID
        redirectUri: 'http://localhost:4200/login/oauth2/code/azure-dev',
      },
    cache: {
      cacheLocation: 'localStorage',
      storeAuthStateInCookie: true,
    },
  };
  