import { Button, ButtonGroup } from 'react-bootstrap';
import { FloppyDiskIcon, TextWrapIcon } from 'hugeicons-react';
import { downloadBase64File } from '../../../utils/files';
import AceEditor from 'react-ace';
import { decodeToText } from '../../../utils/base.64converters';
import React, { useState } from 'react';
import { loadSettings } from '../../../settings/utils';
import { mimeToAceModeMap } from '../../../const/common.const';
import { Headers } from '../../../api/service';
import { getContentTypeFromMap } from '../../../utils/http';

export interface BodyRepresentationProps {
  invocationId: number;
  headers: Headers;
  body: string | null;
}

export const BodyRepresentation: React.FC<BodyRepresentationProps> = ({
                                                                        invocationId,
                                                                        headers,
                                                                        body,
                                                                      }) => {
  const settings = loadSettings();
  const [isWordWrapEnabled, setIsWordWrapEnabled] = useState(true);

  if (!body) return 'N/A';

  const contentType = getContentTypeFromMap(headers) ?? 'text/plain';
  const aceMode = mimeToAceModeMap.get(contentType) || '';
  if (!aceMode) {
    return (
      <Button
        variant="outline-primary"
        onClick={() => downloadBase64File(body, `file.${invocationId}.bin`, contentType)}
      >
        <FloppyDiskIcon />
      </Button>
    );
  }
  return <div className="position-relative">
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
      <Button
        variant="outline-primary"
        onClick={() => downloadBase64File(body, `file.${invocationId}.bin`, contentType)}
      >
        <FloppyDiskIcon />
      </Button>
    </ButtonGroup>
    <AceEditor
      mode={aceMode}
      theme={settings['aceTheme'].value}
      name="contentAceEditor"
      value={decodeToText(body)}
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
        readOnly: true,
      }}
      editorProps={{ $blockScrolling: true }}
    />
  </div>;
};
