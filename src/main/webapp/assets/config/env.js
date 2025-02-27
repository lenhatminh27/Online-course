// Environment-specific settings
const ENV = {
    development: {
        apiUrl: 'http://localhost:8080',
    },
    production: {
        apiUrl: 'https://your-production-url.com',
    },
};
// Determine the current environment
const currentEnv = "development";
// Export the environment configuration
export const environment = ENV[currentEnv];

export const ws = "ws://localhost:8080";

// Storage keys for local storage or session storage
export const STORAGE_KEY = {
    accessToken: 'online_course_access_token',
    refreshToken: 'online_course_refresh_token',
    userCurrent: 'online_course_user_current',
};

export const avatarDefault = 'https://static.vecteezy.com/system/resources/previews/009/292/244/non_2x/default-avatar-icon-of-social-media-user-vector.jpg';