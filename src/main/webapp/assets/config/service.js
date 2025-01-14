import {STORAGE_KEY, environment} from '../config/env.js'

function apiRequestWithToken(url, options = {}) {
    const token = localStorage.getItem(STORAGE_KEY.accessToken);
    // Set default headers and add token if available
    const headers = new Headers(options.headers || {});
    if (token) {
        headers.append('Authorization', `Bearer ${token}`);
    }
    const config = {
        ...options,
        headers,
    };

    return apiRequest(url, config);
}

function apiRequest(url, options = {}) {
    const config = {
        ...options,
        headers: new Headers(options.headers || {}),
    };

    return fetch(url, config)
        .then(response => {
            if (!response.ok) {
                if (response.status === 401) {
                    return handle401Error(url, config);
                } else if (response.status === 403 || response.status === 500) {
                    return response.json().then(errorData => {
                        const error = new Error('API Error');
                        error.response = response;
                        error.data = errorData;
                        throw error;
                    });
                } else {
                    return response.json().then(errorData => {
                        const error = new Error('API Error');
                        error.response = response;
                        error.data = errorData;
                        throw error;
                    });
                }
            }
            return response.json();
        })
        .catch(error => {
            if (error.response) {
                switch (error.response.status) {
                    case 403:
                        alert('You do not have permission to perform this action.');
                        break;
                    case 500:
                        alert('Internal server error. Please try again later.');
                        break;
                    default:
                        throw error; // Let other errors be handled by the caller
                }
            } else {
                alert('No response from the server. Please check your network.');
            }
            throw error;
        });
}

function handle401Error(url, originalConfig) {
    const refreshToken = localStorage.getItem('refresh-token');
    const headers = new Headers();
    headers.append('Authorization', `Bearer ${refreshToken}`);
    return fetch(environment.apiUrl + '/refresh-token', { method: 'GET', headers })
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to refresh token');
            }
            return response.json();
        })
        .then(data => {
            localStorage.setItem('token', data.token);
            originalConfig.headers.set('Authorization', `Bearer ${data.token}`);
            return fetch(url, originalConfig).then(res => res.json());
        })
        .catch(() => {
            window.location.href = '/login';
            throw new Error('Redirecting to login');
        });
}

// Example usage
// apiRequestWithToken('/api/example-endpoint', { method: 'GET' })
//     .then(data => console.log(data))
//     .catch(error => console.error('API call failed:', error));

// apiRequest('/api/public-endpoint', { method: 'GET' })
//     .then(data => console.log(data))
//     .catch(error => console.error('API call failed:', error));

export { apiRequestWithToken, apiRequest };
