import React, { useEffect, useState } from 'react';
import { TextWrapIcon } from 'hugeicons-react';
import { ContentType, mimeToAceModeMap } from '../const/common.const';
import AceEditor from 'react-ace';

import '../const/ace.imports';
import { loadSettings } from '../settings/utils';
import { Button, ButtonGroup, Form } from 'react-bootstrap';

export interface StaticMockContentProps {
  content: ArrayBuffer;
  setContent: (content: ArrayBuffer) => void;
  meta: { [key: string]: string };
  setMeta: (meta: { [key: string]: string }) => void;
  creation: boolean;
}

const textEncoder = new TextEncoder();
const textDecoder = new TextDecoder();

const StaticMockContent: React.FC<StaticMockContentProps> = ({ content, setContent, meta }) => {
  const [aceType, setAceType] = useState('text');
  const settings = loadSettings();
  const [isWordWrapEnabled, setIsWordWrapEnabled] = useState(true);

  useEffect(() => {
    const httpHeadersJson = meta['HTTP_HEADERS'];
    const httpHeaders = httpHeadersJson ? JSON.parse(httpHeadersJson) : null;
    const contentType = (httpHeaders ? httpHeaders['Content-Type'] : null) as ContentType;
    setAceType(mimeToAceModeMap.get(contentType) || 'text');
  }, [meta]);

  return (
    <Form.Group className="mb-3">
      <Form.Label htmlFor="contentTextArea">Content</Form.Label>

      <div className="position-relative">
        {/* Word Wrap Button */}
        <ButtonGroup className="position-absolute" style={{ top: '-20px', right: '-8px', zIndex: 3 }}>
          <Button
            variant="primary"
            active={isWordWrapEnabled}
            title={isWordWrapEnabled ? 'Unwrap' : 'Wrap'}
            onClick={() => setIsWordWrapEnabled((prev) => !prev)}
          >
            <TextWrapIcon />
          </Button>
        </ButtonGroup>

        {/* Ace Editor */}
        <AceEditor
          mode={aceType}
          theme={settings['aceTheme'].value}
          name="contentAceEditor"
          onChange={(it) => setContent(textEncoder.encode(it))}
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
    </Form.Group>
  );
};

export default StaticMockContent;
