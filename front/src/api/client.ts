import axios from 'axios';

const baseURL = window.location.hostname === 'linksuqare.shop' || window.location.hostname === 'www.linksuqare.shop'
  ? '/api'  // 도메인 사용시 프록시 경유
  : 'http://125.131.198.22:8090/api'; // IP 직접 접근시

export const api = axios.create({
  baseURL,
  withCredentials: true,
});

api.interceptors.response.use(
  response => response,
  error => {
    if (error.response?.status === 401) {
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);