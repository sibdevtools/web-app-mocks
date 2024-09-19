import axios from 'axios';
import { Method } from '../const/common.const';

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
export const getMocksByService = (serviceId: number) => api.get(`/services/${serviceId}/mocks`);

// Create a new mock
export type MockType = 'STATIC' | 'JS'

export interface MockMeta {
  [key: string]: string
}

export interface CreateMockRq {
  name: string
  method: Method
  antPattern: string
  type: MockType,
  meta: MockMeta
  content: string
}

export const createMock = (serviceId: number, rq: CreateMockRq) => api.post(`/services/${serviceId}/mocks/`, rq);

// Fetch mock
export interface GetMockRs {
  success: boolean
  body: {
    serviceId: number,
    mockId: number,
    method: Method
    name: string,
    antPattern: string,
    type: MockType,
    meta: MockMeta
    content: string
  }
}


export const getMock = (serviceId: number, mockId: number) => api.get<GetMockRs>(`/services/${serviceId}/mocks/${mockId}`);

export interface UpdateMockRq {
  name: string
  method: Method
  antPattern: string
  type: MockType
  meta: MockMeta
  content: string
}

export const updateMock = (serviceId: number, mockId: number, rq: UpdateMockRq) => api.put(`/services/${serviceId}/mocks/${mockId}`, rq);

// Delete a mock (assuming you have a delete endpoint, otherwise skip this)
export const deleteMock = (serviceId: number, mockId: number) => api.delete(`/services/${serviceId}/mocks/${mockId}`);


