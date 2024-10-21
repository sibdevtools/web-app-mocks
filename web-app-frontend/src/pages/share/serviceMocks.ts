import { useEffect, useState } from 'react';
import { deleteMock, getMocksByService, Mock, setEnabledMock } from '../../api/service';


export interface Service {
  serviceId: number;
  code: string;
}

export interface ExportingMock extends Mock{
  exporting: boolean;
}

export const useServiceMocks = (serviceId: string | undefined, setLoading: (loading: boolean) => void) => {
  const [service, setService] = useState<Service>({ code: '', serviceId: 0 });
  const [mocks, setMocks] = useState<ExportingMock[]>([]);

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
        const mocks = body.mocks.map(it => {
          return {
            ...it,
            exporting: true
          }
        })
        setMocks(mocks);
      }
    } catch (error) {
      console.error('Failed to fetch mocks:', error);
    } finally {
      setLoading(false);
    }
  };

  const setExportingMockHandler = async (mock: ExportingMock, exporting: boolean) => {
    mock.exporting = exporting;
    setMocks([...mocks.filter(it => it.mockId !== mock.mockId), mock]);
  };

  return { service, mocks, setExportingMockHandler };
};
