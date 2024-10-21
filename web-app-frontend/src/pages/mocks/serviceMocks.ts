import { useEffect, useState } from 'react';
import { deleteMock, getMocksByService, Mock, setEnabledMock } from '../../api/service';


export interface Service {
  serviceId: number;
  code: string;
}

export const useServiceMocks = (serviceId: string | undefined, setLoading: (loading: boolean) => void) => {
  const [service, setService] = useState<Service>({ code: '', serviceId: 0 });
  const [mocks, setMocks] = useState<Mock[]>([]);

  useEffect(() => {
    if (serviceId) {
      fetchMocks();
    }
  }, [serviceId]);

  const fetchMocks = async () => {
    setLoading(true);
    try {
      const response = await getMocksByService(+serviceId!);
      const body = response.data.body;
      if (response.data.success) {
        setService({
          code: body.code,
          serviceId: body.serviceId
        });
        setMocks(body.mocks);
      }
    } catch (error) {
      console.error('Failed to fetch mocks:', error);
    } finally {
      setLoading(false);
    }
  };

  const deleteMockHandler = async (mock: Mock) => {
    if (!window.confirm('Are you sure?')) {
      return;
    }
    try {
      const response = await deleteMock(service.serviceId, mock.mockId);
      if (response.status !== 200 || !response.data.success) {
        console.error('Failed to delete mock');
        return;
      }
      setMocks(mocks.filter(it => it.mockId !== mock.mockId));
    } catch (error) {
      console.error('Failed to delete mock:', error);
    }
  };

  const setEnabledMockHandler = async (mock: Mock, enabled: boolean) => {
    try {
      const response = await setEnabledMock(service.serviceId, mock.mockId, { enabled });
      if (response.status !== 200 || !response.data.success) {
        console.error('Failed to set mock enabled');
        mock.enabled = !enabled;
        setMocks([...mocks.filter(it => it.mockId !== mock.mockId), mock]);
        return;
      }
      mock.enabled = enabled;
      setMocks([...mocks.filter(it => it.mockId !== mock.mockId), mock]);
    } catch (error) {
      console.error('Failed to set mock enabled:', error);
    }
  };

  return { service, mocks, deleteMockHandler, setEnabledMockHandler };
};
