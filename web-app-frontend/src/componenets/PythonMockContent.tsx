import React, { useState } from 'react';
import { InformationCircleIcon, TextWrapIcon } from 'hugeicons-react';
import AceEditor from 'react-ace';

import '../const/ace.imports';
import './modal/modal.css'
import { loadSettings } from '../settings/utils';

export interface PythonMockContentProps {
  content: ArrayBuffer;
  setContent: (content: ArrayBuffer) => void;
}

const textEncoder = new TextEncoder();
const textDecoder = new TextDecoder();

const PythonMockContent: React.FC<PythonMockContentProps> = ({
                                                               content,
                                                               setContent
                                                             }) => {
  const settings = loadSettings();
  const [isWordWrapEnabled, setIsWordWrapEnabled] = useState(true);
  const [isModalVisible, setIsModalVisible] = useState(false);

  const showModal = () => setIsModalVisible(true);
  const hideModal = () => setIsModalVisible(false);

  return (
    <div className="mb-3">
      <label htmlFor={`contentTextArea`} className="form-label">Code</label>
      <div className="position-relative">
        <div className="btn-group position-absolute" role="group" style={{ top: '-20px', right: '-8px', zIndex: 3 }}>
          {/* Word wrap button */}
          <button
            className={`btn btn-primary ${isWordWrapEnabled ? 'active' : ''}`}
            title={isWordWrapEnabled ? 'Unwrap' : 'Wrap'}
            type="button"
            onClick={() => setIsWordWrapEnabled((prev) => !prev)}
          >
            <TextWrapIcon />
          </button>
          <button
            className="btn btn-info"
            title="Code Reference"
            type="button"
            onClick={showModal}
          >
            <InformationCircleIcon />
          </button>
        </div>
        <div
          id={`contentTextArea`}
          style={{ position: 'relative' }}
          aria-describedby={`contentTextAreaFeedback`}
          className="form-control"
        >
          <AceEditor
            mode="python"
            theme={settings['aceTheme'].value}
            name={`contentAceEditor`}
            onChange={it => setContent(textEncoder.encode(it))}
            value={textDecoder.decode(content)}
            fontSize={14}
            width="100%"
            height="480px"
            showPrintMargin={true}
            showGutter={true}
            highlightActiveLine={true}
            wrapEnabled={isWordWrapEnabled}
            setOptions={{
              enableBasicAutocompletion: true,
              enableLiveAutocompletion: true,
              showLineNumbers: true,
              enableSnippets: true,
              wrap: isWordWrapEnabled,
              useWorker: false,
            }}
            editorProps={{ $blockScrolling: true }}
          />
        </div>

        {/* Modal for the code reference */}
        {isModalVisible && (
          <div className="custom-modal-overlay">
            <div className="custom-modal">
              <div className="custom-modal-header">
                <h5>Code Reference</h5>
                <button className="close-btn" onClick={hideModal}>&times;</button>
              </div>
              <div className="custom-modal-body">
                <h5>WAM Entry Point</h5>
                <p>The `wam` object provides three methods:</p>
                <ul>
                  <li><strong>request</strong> - returns current HTTP request, type <code>GraalVMRequest</code></li>
                  <li><strong>response</strong> - returns current HTTP response, type <code>GraalVMResponse</code></li>
                  <li><strong>sessions</strong> - returns session service</li>
                </ul>
                <h5>GraalVMRequest</h5>
                <p>Fields:</p>
                <ul>
                  <li><strong>remoteHost</strong></li>
                  <li><strong>remoteAddress</strong></li>
                </ul>
                <p>Methods:</p>
                <ul>
                  <li><strong>header</strong> - retrieves headers from the request</li>
                  <li><strong>queryParams</strong> - retrieves query parameters</li>
                </ul>
                <h5>GraalVMResponse</h5>
                <p>Methods:</p>
                <ul>
                  <li><strong>status</strong> - change response status</li>
                  <li><strong>header</strong> - change response header</li>
                </ul>
              </div>
              <div className="custom-modal-footer">
                <button className="btn btn-secondary" onClick={hideModal}>Close</button>
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default PythonMockContent;
