import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'hugeicons-react'
import { BrowserRouter, Route, Routes } from 'react-router-dom';
//
// import 'ace-builds/src-noconflict/mode-json';
// import 'ace-builds/src-noconflict/mode-xml';
// import 'ace-builds/src-noconflict/mode-html';
// import 'ace-builds/src-noconflict/mode-javascript';
// import 'ace-builds/src-min-noconflict/mode-json.js'
//
// import 'ace-builds/src-noconflict/theme-monokai';
// import 'ace-builds/src-noconflict/theme-github';
// import 'ace-builds/src-noconflict/theme-tomorrow';
// import 'ace-builds/src-noconflict/theme-kuroir';
// import 'ace-builds/src-noconflict/theme-twilight';
// import 'ace-builds/src-noconflict/theme-xcode';
// import 'ace-builds/src-noconflict/theme-textmate';
// import 'ace-builds/src-noconflict/theme-solarized_dark';
// import 'ace-builds/src-noconflict/theme-solarized_light';
// import 'ace-builds/src-noconflict/theme-terminal';
//
// import 'ace-builds/src-noconflict/ext-language_tools';

import { ThemeProvider } from './theme/ThemeContext';
import ServiceListPage from './pages/service/ServiceListPage';
import AddServiceForm from './pages/service/AddServicePage';
import EditServicePage from './pages/service/EditServicePage';
import ServiceMocksListPage from './pages/service/ServiceMocksListPage';
import { contextPath } from './const/common.const';


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
                <Route path={'mocks'} element={<ServiceMocksListPage />} />
              </Route>
            </Route>
          </Route>
        </Routes>
      </BrowserRouter>
    </ThemeProvider>
  );
};

export default App;
