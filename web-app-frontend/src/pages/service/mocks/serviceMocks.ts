import { useEffect, useState } from 'react';
import { deleteMock, getMocksByService, Mock, ServiceV2, setEnabledMock } from '../../../api/service';

export const useServiceMocks = (serviceId: string | undefined, setLoading: (loading: boolean) => void) => {
  const [service, setService] = useState<ServiceV2>({ code: '', mocks: [], serviceId: 0 });

  useEffect(() => {
    if (serviceId) {
      fetchMocks();
    }
  }, [serviceId]);

  const fetchMocks = async () => {
    setLoading(true);
    try {
      const response = await getMocksByService(+serviceId!);
      if (response.data.success) {
        setService(response.data.body);
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
      await deleteMock(service.serviceId, mock.mockId);
      await fetchMocks();
    } catch (error) {
      console.error('Failed to delete mock:', error);
    }
  };

  const setEnabledMockHandler = async (mock: Mock, enabled: boolean) => {
    try {
      await setEnabledMock(service.serviceId, mock.mockId, { enabled });
      await fetchMocks();
    } catch (error) {
      console.error('Failed to set mock enabled:', error);
    }
  };

  return { service, deleteMockHandler, setEnabledMockHandler, fetchMocks };
};
