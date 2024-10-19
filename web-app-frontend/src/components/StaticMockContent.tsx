import React, { useEffect, useState } from 'react';
import { TextWrapIcon } from 'hugeicons-react';
import { mimeToAceModeMap } from '../const/common.const';
import AceEditor from 'react-ace';

import '../const/ace.imports';
import { loadSettings } from '../settings/utils';
import { Button, ButtonGroup, Form } from 'react-bootstrap';
import { getContentType } from '../utils/http';

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
    const contentType = getContentType(httpHeadersJson);

    setAceType(mimeToAceModeMap.get(contentType ?? 'plain/text') ?? 'text');
  }, [meta]);

  return (
    <Form.Group className="mb-3">
      <Form.Label htmlFor="contentTextArea">Content</Form.Label>

      {/* Word Wrap Button */}
      <ButtonGroup className={'float-end'}>
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
        className={'rounded'}
        style={{
          resize: 'vertical',
          overflow: 'auto',
          height: '480px',
          minHeight: '200px',
        }}
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
    </Form.Group>
  );
};

export default StaticMockContent;
