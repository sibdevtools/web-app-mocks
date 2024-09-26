import React, { useState } from 'react';
import { TextWrapIcon } from 'hugeicons-react';
import AceEditor from 'react-ace';
import { useTheme } from '../theme/ThemeContext';

import '../const/ace.imports'

export interface JavaScriptMockContentProps {
  content: ArrayBuffer,
  setContent: (content: ArrayBuffer) => void,
}

const textEncoder = new TextEncoder();
const textDecoder = new TextDecoder();

const JavaScriptMockContent: React.FC<JavaScriptMockContentProps> = ({
                                                                       content,
                                                                       setContent
                                                                     }) => {
  const { theme } = useTheme();
  const [isWordWrapEnabled, setIsWordWrapEnabled] = useState(true);

  return (
    <div className={'mb-3'}>
      <label htmlFor={`contentTextArea`} className="form-label">Content</label>
      <div className={' position-relative'}>
        <div className="btn-group position-absolute" role="group"
             style={{ top: '-20px', right: '-8px', zIndex: 3 }}>
          <button
            className={`btn btn-primary ${(isWordWrapEnabled ? 'active' : '')}`}
            title={isWordWrapEnabled ? 'Unwrap' : 'Wrap'}
            type={'button'}
            onClick={_ => setIsWordWrapEnabled((prev) => !prev)}
          >
            <TextWrapIcon />
          </button>
        </div>
        <div id={`contentTextArea`}
             style={{ position: 'relative' }}
             aria-describedby={`contentTextAreaFeedback`}
             className={`form-control`}
        >
          <AceEditor
            mode={'javascript'}
            theme={theme}
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
              useWorker: false
            }}
            editorProps={{ $blockScrolling: true }}
          />
        </div>
      </div>
    </div>
  );
};

export default JavaScriptMockContent;
