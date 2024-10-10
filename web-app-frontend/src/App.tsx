import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'hugeicons-react'
import { BrowserRouter, Route, Routes } from 'react-router-dom';

import ServiceListPage from './pages/service/ServiceListPage';
import ServiceMocksListPage from './pages/service/mocks/ServiceMocksListPage';
import { contextPath } from './const/common.const';
import AddMockPage from './pages/mock/AddEditMockPage';
import AddEditMockPage from './pages/mock/AddEditMockPage';
import MockInvocationListPage from './pages/invocations/MockInvocationListPage';


const App: React.FC = () => {
  return (
    <BrowserRouter>
      <Routes>
        <Route path={contextPath}>
          <Route index element={<ServiceListPage />} />
          <Route path="service">
            <Route path={':serviceId'}>
              <Route path={'mocks'}>
                <Route index element={<ServiceMocksListPage />} />
                <Route path={'add'} element={<AddMockPage />} />
                <Route path={'edit'}>
                  <Route path={':mockId'} element={<AddEditMockPage />} />
                </Route>
                <Route path={'invocations'}>
                  <Route path={':mockId'} element={<MockInvocationListPage />} />
                </Route>
              </Route>
            </Route>
          </Route>
        </Route>
      </Routes>
    </BrowserRouter>
  );
};

export default App;
