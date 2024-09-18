import React, { useEffect, useState } from 'react';
import { deleteMock, getMocksByService } from '../../services/api';
import { Delete01Icon, PencilEdit01Icon, PlusSignIcon } from 'hugeicons-react';
import { useNavigate, useParams } from 'react-router-dom';
import { contextPath } from '../../const/common.const';

interface Mock {
  mockId: number;
  method: string;
  antPattern: string;
  type: string;
}

interface Service {
  serviceId: number;
  code: string;
  mocks: Mock[];
}

const ServiceMocksListPage: React.FC = () => {
  const [service, setService] = useState<Service>({ code: '', mocks: [], serviceId: 0 });
  const navigate = useNavigate();
  const { serviceId } = useParams();

  if (!serviceId) {
    navigate(contextPath);
    return;
  }

  useEffect(() => {
    fetchService();
  }, []);

  const fetchService = async () => {
    try {
      const response = await getMocksByService(+serviceId);
      if (response.data.success) {
        setService(response.data.body);
      }
    } catch (error) {
      console.error('Failed to fetch mocks:', error);
    }
  };

  const handleDelete = async (mockId: number) => {
    if (!window.confirm('Are you sure?')) {
      return;
    }
    try {
      await deleteMock(service.serviceId, mockId);
      fetchService();
    } catch (error) {
      console.error('Failed to delete mock:', error);
    }
  };

  const handleEdit = async (service: Service, mock: Mock) => {
    navigate(`${contextPath}service/${service.serviceId}/mocks/edit/${mock.mockId}`, {
      state: {
        code: service.code
      }
    });
  };

  const [filter, setFilter] = useState('');

  const filteredMocks = service.mocks.filter((mock) =>
    mock.antPattern.includes(filter)
  );

  return (
    <div className="container mt-4">
      <div className={'row'}>
        <div className="col-md-8 offset-md-2">
          <div className="row g-3">
            <div className="col-md-10">
              <label htmlFor="filterInput" className="h2">Filter</label>
              <input
                id={'filterInput'}
                type="text"
                placeholder="Ant path"
                className="form-control mb-3"
                value={filter}
                onChange={(e) => setFilter(e.target.value)}
              />
            </div>
          </div>
          <div className="row mb-2">
            <div className="col-md-10">
              <span className={'h2'}>HTTP Service {service.code} Mocks</span>
            </div>
            <div className="col-md-1 offset-md-1">
              <button className="btn btn-outline-success"
                      onClick={() => navigate(`${contextPath}service/${service.serviceId}/mocks/add`)}>
                <PlusSignIcon />
              </button>
            </div>
          </div>
          <ul className="row list-group">
            {filteredMocks.map((mock) => (
              <li key={mock.mockId} className="list-group-item d-flex row">
                <div className="col-md-2 text-center">
                  <span className={'badge text-bg-primary align-middle'}>
                  {mock.method}
                  </span>
                </div>
                <div className="col-md-6 text-center">
                  <span className={'align-middle'}>
                  <pre>
                    <code>
                    {mock.antPattern}
                    </code>
                  </pre>
                  </span>
                </div>
                <div className="col-md-2 text-center">
                  <span className={'align-middle'}>
                  {mock.type}
                  </span>
                </div>
                <div className="col-md-2">
                  <div className={'row'}>
                    <div className={'col-md-6'}>
                      <button className="btn btn-primary" onClick={() => handleEdit(service, mock)}>
                        <PencilEdit01Icon />
                      </button>
                    </div>
                    <div className={'col-md-6'}>
                      <button className="btn btn-danger" onClick={() => handleDelete(mock.mockId)}>
                        <Delete01Icon />
                      </button>
                    </div>
                  </div>
                </div>
              </li>
            ))}
          </ul>
        </div>
      </div>
    </div>
  );
};

export default ServiceMocksListPage;
