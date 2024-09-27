import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'hugeicons-react'
import { BrowserRouter, Route, Routes } from 'react-router-dom';

import ServiceListPage from './pages/service/ServiceListPage';
import AddServiceForm from './pages/service/AddServicePage';
import EditServicePage from './pages/service/EditServicePage';
import ServiceMocksListPage from './pages/service/ServiceMocksListPage';
import AddMockPage from './pages/mock/AddMockPage';
import EditMockPage from './pages/mock/EditMockPage';


const App: React.FC = () => {
  return (
    <BrowserRouter>
      <Routes>
        <Route index element={<ServiceListPage />} />
        <Route path="service">
          <Route path={'add'} element={<AddServiceForm />} />
          <Route path={'edit'}>
            <Route path={':serviceId'} element={<EditServicePage />} />
          </Route>
          <Route path={':serviceId'}>
            <Route path={'mocks'}>
              <Route index element={<ServiceMocksListPage />} />
              <Route path={'add'} element={<AddMockPage />} />
              <Route path={'edit'}>
                <Route path={':mockId'} element={<EditMockPage />} />
              </Route>
            </Route>
          </Route>
        </Route>
      </Routes>
    </BrowserRouter>
  );
};

export default App;
