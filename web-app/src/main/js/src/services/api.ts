import axios from 'axios';

const api = axios.create({
  baseURL: '/web/app/mocks/rest/api',
  headers: {
    'Content-Type': 'application/json',
  },
});

// Fetch all services
export const getAllServices = () => api.get('/services/');

// Create a new service
export const createService = (rq: { code: string }) => api.post('/services/', rq);
// Update an existing service
export const updateService = (rq: { serviceId: number, code: string }) => api.put('/services/', rq);

// Delete a service (assuming you have a delete endpoint, otherwise skip this)
export const deleteService = (serviceId: number) => api.delete(`/services/${serviceId}`);

// Fetch mocks for a service
export const getMocksByService = (serviceId: number) => api.post('/mocks/v1/get', { serviceId });

// Create a new mock
export const createMock = (mockData: any) => api.post('/mocks/v1/create', mockData);

// Update a mock
export const updateMock = (mockData: any) => api.post('/mocks/v1/update', mockData);

// Delete a mock (assuming you have a delete endpoint, otherwise skip this)
export const deleteMock = (mockId: number) => api.post(`/mocks/v1/delete`, { mockId });
