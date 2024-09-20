import axios from 'axios';
import { Method, MockType } from '../const/common.const';

const service = axios.create({
  baseURL: '/web/app/mocks/rest/api',
  headers: {
    'Content-Type': 'application/json',
  },
});


export interface Service {
  serviceId: number;
  code: string;
}

export interface GetAllServicesRs {
  success: boolean;
  body: Service[];
}

// Fetch all services
export const getAllServices = () => service.get<GetAllServicesRs>('/services/');

// Create a new service
export const createService = (rq: { code: string }) => service.post('/services/', rq);
// Update an existing service
export const updateService = (rq: { serviceId: number, code: string }) => service.put('/services/', rq);

// Delete a service (assuming you have a delete endpoint, otherwise skip this)
export const deleteService = (serviceId: number) => service.delete(`/services/${serviceId}`);


export interface Mock {
  mockId: number;
  method: string;
  name: string;
  antPattern: string;
  type: string;
  enabled: boolean;
}

export interface ServiceV2 {
  serviceId: number;
  code: string;
  mocks: Mock[];
}

export interface GetMocksByServiceRs {
  success: boolean;
  body: ServiceV2;
}

// Fetch mocks for a service
export const getMocksByService = (serviceId: number) => service.get<GetMocksByServiceRs>(`/services/${serviceId}/mocks`);

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

export const createMock = (serviceId: number, rq: CreateMockRq) => service.post(`/services/${serviceId}/mocks/`, rq);

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


export const getMock = (serviceId: number, mockId: number) => service.get<GetMockRs>(`/services/${serviceId}/mocks/${mockId}`);

export interface UpdateMockRq {
  name: string
  method: Method
  antPattern: string
  type: MockType
  meta: MockMeta
  content: string
}

export const updateMock = (serviceId: number, mockId: number, rq: UpdateMockRq) => service.put(`/services/${serviceId}/mocks/${mockId}`, rq);

// Delete a mock (assuming you have a delete endpoint, otherwise skip this)
export const deleteMock = (serviceId: number, mockId: number) => service.delete(`/services/${serviceId}/mocks/${mockId}`);

export interface GetMockUrlRs {
  success: boolean
  body: string
}
export const getMockUrl = (serviceId: number, mockId: number) => service.get<GetMockUrlRs>(`/services/${serviceId}/mocks/${mockId}/url`);


export interface SetEnabledMockRq {
  enabled: boolean
}

export const setEnabledMock = (serviceId: number, mockId: number, rq: SetEnabledMockRq) => service.put(`/services/${serviceId}/mocks/${mockId}/enabled`, rq);

