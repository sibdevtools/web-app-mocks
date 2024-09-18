import React, { useEffect, useState } from 'react';
import { getAllServices, deleteService } from '../../services/api';
import { Delete01Icon, PencilEdit01Icon, PlusSignIcon } from 'hugeicons-react';
import { useNavigate } from 'react-router-dom';
import { contextPath } from '../../const/common.const';

interface Service {
  serviceId: number;
  code: string;
}

const ServiceListPage: React.FC = () => {
  const [services, setServices] = useState<Service[]>([]);
  const navigate = useNavigate();

  useEffect(() => {
    fetchServices();
  }, []);

  const fetchServices = async () => {
    try {
      const response = await getAllServices();
      if (response.data.success) {
        setServices(response.data.body);
      }
    } catch (error) {
      console.error('Failed to fetch services:', error);
    }
  };

  const handleDelete = async (serviceId: number) => {
    if (!window.confirm('Are you sure?')) {
      return;
    }
    try {
      await deleteService(serviceId);
      fetchServices();
    } catch (error) {
      console.error('Failed to delete service:', error);
    }
  };

  const handleEdit = async (service: Service) => {
    navigate(`${contextPath}service/edit/${service.serviceId}`, {
      state: {
        code: service.code
      }
    });
  };

  const [filter, setFilter] = useState('');

  const filteredServices = services.filter((service) =>
    service.code.includes(filter)
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
                placeholder="Filter..."
                className="form-control mb-3"
                value={filter}
                onChange={(e) => setFilter(e.target.value)}
              />
            </div>
          </div>
          <div className="row mb-2">
            <div className="col-md-10">
              <span className={'h2'}>HTTP Services</span>
            </div>
            <div className="col-md-1 offset-md-1">
              <button className="btn btn-outline-success" onClick={() => navigate(`${contextPath}service/add`)}>
                <PlusSignIcon />
              </button>
            </div>
          </div>
          <ul className="row list-group">
            {filteredServices.map((service) => (
              <li key={service.serviceId} className="list-group-item d-flex row">
                <div className="col-md-10">
                  <a href={`service/${service.serviceId}/mocks`} className="list-group-item list-group-item-action">
                    {service.code}
                  </a>
                </div>
                <div className="col-md-2">
                  <div className={'row'}>
                    <div className={'col-md-6'}>
                      <button className="btn btn-primary" onClick={() => handleEdit(service)}>
                        <PencilEdit01Icon />
                      </button>
                    </div>
                    <div className={'col-md-6'}>
                      <button className="btn btn-danger" onClick={() => handleDelete(service.serviceId)}>
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

export default ServiceListPage;
