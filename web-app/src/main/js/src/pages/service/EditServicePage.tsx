import React, { useState } from 'react';
import { updateService } from '../../api/service';
import { useLocation, useNavigate, useParams } from 'react-router-dom';
import { ArrowLeft01Icon, FloppyDiskIcon } from 'hugeicons-react';
import { contextPath } from '../../const/common.const';

interface EditServiceState {
  code: string
}

const AddServicePage: React.FC = () => {
  const navigate = useNavigate();
  const { serviceId } = useParams();

  const { state } = useLocation();
  const editServiceState = state as EditServiceState | null;

  const [code, setCode] = useState(editServiceState?.code);

  if (!serviceId || !code) {
    navigate(contextPath);
    return;
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await updateService({ serviceId: +serviceId, code });
      navigate(contextPath);
    } catch (error) {
      console.error('Failed to create service:', error);
    }
  };

  return (
    <div className="container mt-4 mb-4">
      <div className={'row'}>
        <div className={'col-md-1 offset-md-2 mb-2'}>
          <button type="button" className="btn btn-outline-primary" onClick={() => navigate(contextPath)}>
            <ArrowLeft01Icon />
          </button>
        </div>
        <div className={'col-md-9'}>
          <h2>Edit Service</h2>
        </div>
      </div>
      <div className={'row'}>
        <div className="col-md-8 offset-md-2">
          <form onSubmit={handleSubmit}>
            <div className="mb-3">
              <label htmlFor="serviceCode" className="form-label">Service Code</label>
              <input
                type="text"
                className="form-control"
                id="serviceCode"
                value={code}
                onChange={(e) => setCode(e.target.value)}
              />
            </div>
            <div className={'col-md-1 offset-md-11'}>
              <button type="submit" className="btn btn-primary">
                <FloppyDiskIcon />
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default AddServicePage;
