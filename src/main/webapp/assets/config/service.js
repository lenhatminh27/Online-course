import {STORAGE_KEY, environment} from '../config/env.js'

function apiRequestWithToken(url, options = {}) {
    const token = localStorage.getItem(STORAGE_KEY.accessToken);
    const headers = new Headers(options.headers || {});
    if (token) {
        headers.append('Authorization', `Bearer ${token}`);
    };
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
            console.log('Response Status:', response.status);
            if (response.status === 204) {
                return {};
            }
            if (!response.ok) {
                if (response.status === 401) {
                    return handle401Error(url, config);
                }
                return response.json().then(errorData => {
                    const error = new Error('API Error');
                    error.response = response;
                    error.data = errorData;
                    throw error;
                });
            }
            return response.json();
        })
        .catch(error => {
            console.log('Catch block triggered:', error);
            if (error.response) {
                switch (error.response.status) {
                    case 404:
                        window.location.assign('/404');
                        break;
                    case 403:
                        window.location.assign('/403');
                        break;
                    case 500:
                        window.location.assign('/500');
                        break;
                    default:
                        throw error;
                }
            } else {
                alert('No response from the server. Please check your network.');
            }
            throw error;
        });
}

function handle401Error(url, originalConfig) {
    const refreshToken = localStorage.getItem(STORAGE_KEY.refreshToken);
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
            localStorage.setItem(STORAGE_KEY.accessToken, data.accessToken);
            localStorage.setItem(STORAGE_KEY.refreshToken, data.refreshToken);
            originalConfig.headers.set('Authorization', `Bearer ${data.token}`);
            return fetch(url, originalConfig).then(res => res.json());
        })
        .catch(() => {
            localStorage.removeItem(STORAGE_KEY.accessToken);
            localStorage.removeItem(STORAGE_KEY.refreshToken);
            localStorage.removeItem(STORAGE_KEY.userCurrent);
            window.location.href = '/login';
        });
}

export { apiRequestWithToken, apiRequest };
