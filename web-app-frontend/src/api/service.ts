import axios from 'axios';
import { MockType } from '../const/common.const';

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
export interface CreateServiceRq {
  code: string;
}

export interface CreateServiceRs {
  success: boolean;
  body: number;
}

export const createService = (rq: CreateServiceRq) => service.post<CreateServiceRs>('/services/', rq);

// Update an existing service

export interface UpdateServiceRq {
  code: string;
}

export interface UpdateServiceRs {
  success?: boolean;
}

export const updateService = (serviceId: number, rq: UpdateServiceRq) => service.put<UpdateServiceRs>(
  `/services/${serviceId}`,
  rq
);

export interface DeleteServiceRs {
  success?: boolean;
}

// Delete a service (assuming you have a delete endpoint, otherwise skip this)
export const deleteService = (serviceId: number) => service.delete<DeleteServiceRs>(`/services/${serviceId}`);


export interface Mock {
  mockId: number;
  method: string;
  name: string;
  path: string;
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
  [key: string]: string;
}

export interface CreateMockRq {
  name: string;
  method: string;
  path: string;
  type: MockType;
  delay: number,
  meta: MockMeta;
  content: string;
}

export const createMock = (serviceId: number, rq: CreateMockRq) => service.post(`/services/${serviceId}/mocks/`, rq);

// Fetch mock
export interface GetMockRs {
  success: boolean;
  body: {
    serviceId: number;
    mockId: number;
    method: string;
    name: string;
    path: string;
    type: MockType;
    delay: number,
    meta: MockMeta;
    content: string;
  };
}


export const getMock = (serviceId: number, mockId: number) => service.get<GetMockRs>(`/services/${serviceId}/mocks/${mockId}`);

export interface UpdateMockRq {
  name: string;
  method: string;
  path: string;
  type: MockType;
  delay: number;
  meta: MockMeta;
  content: string;
}

export const updateMock = (serviceId: number, mockId: number, rq: UpdateMockRq) => service.put(
  `/services/${serviceId}/mocks/${mockId}`,
  rq
);

export interface DeleteMockRs {
  success: boolean;
}

// Delete a mock (assuming you have a delete endpoint, otherwise skip this)
export const deleteMock = (
  serviceId: number,
  mockId: number
) => service.delete<DeleteMockRs>(`/services/${serviceId}/mocks/${mockId}`);

export interface GetMockUrlRs {
  success: boolean;
  body: string;
}

export const getMockUrl = (serviceId: number, mockId: number) => service.get<GetMockUrlRs>(`/services/${serviceId}/mocks/${mockId}/url`);


export interface SetEnabledMockRq {
  enabled: boolean;
}


export interface SetEnabledMockRs {
  success: boolean;
}

export const setEnabledMock = (
  serviceId: number,
  mockId: number,
  rq: SetEnabledMockRq
) => service.put<SetEnabledMockRs>(`/services/${serviceId}/mocks/${mockId}/enabled`, rq);

export interface MockInvocationItem {
  invocationId: number;
  method: string;
  path: string;
  timing: number;
  status: number;
  createdAt: string;
}

export interface GetInvocationsByMockRs {
  success: boolean;
  body: {
    pages: number;
    invocations: MockInvocationItem[];
  };
}

// Fetch invocations for a mock
export const getInvocationsByMock = (serviceId: number, mockId: number, page: number, pageSize: number) =>
  service.get<GetInvocationsByMockRs>(`/services/${serviceId}/mocks/${mockId}/history/${pageSize}/${page}`);

export type MultiValueMap = { [key: string]: [string | null] } | null;
export type Headers = MultiValueMap;
export type QueryParams = MultiValueMap;

export interface MockInvocation {
  invocationId: number;
  remoteHost: string | null;
  remoteAddress: string | null;
  method: string;
  path: string;
  queryParams: QueryParams;
  timing: number;
  status: number;
  createdAt: string;
  rqBody: string | null;
  rqHeaders: Headers;
  rsBody: string | null;
  rsHeaders: Headers;
}

export const MockInvocationDefaults: MockInvocation = {
  createdAt: '',
  invocationId: 0,
  method: '',
  path: '',
  queryParams: null,
  remoteAddress: null,
  remoteHost: null,
  rqBody: null,
  rqHeaders: null,
  rsBody: null,
  rsHeaders: null,
  status: 0,
  timing: 0
};

export interface GetInvocationRs {
  success: boolean;
  body: MockInvocation;
}

// Fetch invocation
export const getInvocation = (serviceId: number, mockId: number, invocationId: number) =>
  service.get<GetInvocationRs>(`/services/${serviceId}/mocks/${mockId}/history/invocation/${invocationId}`);


export interface ExportRq {
  mocksIds: number[];
}

export interface ExportedMock {
  method: string;
  name: string;
  path: string;
  type: string;
  delay: number;
  enabled: boolean;
  content: string;
  contentMetadata: { [key: string]: string };
}

export interface ExportedService {
  code: string;
  mocks: ExportedMock[];
}

export interface Exported {
  version: string;
  createdAt: string;
  services: ExportedService[];
}

export interface ExportRs {
  success: boolean;
  body: Exported;
}

// Export selected mocks
export const exportMocks = (rq: ExportRq) => service.post<ExportRs>(`/share/v1/export`, rq);

export interface ImportRq {
  services: ExportedService[];
}

export interface ImportRs {
  success: boolean;
}

// Import mocks
export const importMocks = (rq: ImportRq) => service.post<ImportRs>(`/share/v1/import`, rq);
