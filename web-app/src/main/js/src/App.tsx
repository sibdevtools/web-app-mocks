import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'hugeicons-react'
import { BrowserRouter, Route, Routes } from 'react-router-dom';

import 'ace-builds/src-min-noconflict/ace';
import 'ace-builds/src-min-noconflict/mode-json';
import 'ace-builds/src-min-noconflict/mode-xml';
import 'ace-builds/src-min-noconflict/mode-css';
import 'ace-builds/src-min-noconflict/mode-plain_text';
import 'ace-builds/src-min-noconflict/mode-markdown';
import 'ace-builds/src-min-noconflict/mode-html';
import 'ace-builds/src-min-noconflict/mode-javascript';

import 'ace-builds/src-min-noconflict/theme-monokai';
import 'ace-builds/src-min-noconflict/theme-github';
import 'ace-builds/src-min-noconflict/theme-tomorrow';
import 'ace-builds/src-min-noconflict/theme-kuroir';
import 'ace-builds/src-min-noconflict/theme-twilight';
import 'ace-builds/src-min-noconflict/theme-xcode';
import 'ace-builds/src-min-noconflict/theme-textmate';
import 'ace-builds/src-min-noconflict/theme-solarized_dark';
import 'ace-builds/src-min-noconflict/theme-solarized_light';
import 'ace-builds/src-min-noconflict/theme-terminal';

import 'ace-builds/src-min-noconflict/ext-language_tools';

import { ThemeProvider } from './theme/ThemeContext';
import ServiceListPage from './pages/service/ServiceListPage';
import AddServiceForm from './pages/service/AddServicePage';
import EditServicePage from './pages/service/EditServicePage';
import ServiceMocksListPage from './pages/service/ServiceMocksListPage';
import { contextPath } from './const/common.const';
import AddMockPage from './pages/mock/AddMockPage';


const App: React.FC = () => {
  return (
    <ThemeProvider>
      <BrowserRouter>
        <Routes>
          <Route path={contextPath}>
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
                </Route>
              </Route>
            </Route>
          </Route>
        </Routes>
      </BrowserRouter>
    </ThemeProvider>
  );
};

export default App;
