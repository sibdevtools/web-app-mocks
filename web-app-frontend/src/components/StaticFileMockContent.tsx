import React, { useCallback } from 'react';
import { Button, Form, InputGroup } from 'react-bootstrap';
import { Download05Icon } from 'hugeicons-react';
import { downloadBase64File } from '../utils/files';
import { encode } from '../utils/base64';

export interface StaticMockBinaryContentProps {
  isEditMode: boolean;
  content: ArrayBuffer;
  setContent: (content: ArrayBuffer) => void;
}

const StaticFileMockContent: React.FC<StaticMockBinaryContentProps> = ({
                                                                         isEditMode,
                                                                         content,
                                                                         setContent
                                                                       }) => {
  const getFileContent = useCallback((file: File): Promise<ArrayBuffer> => {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.readAsArrayBuffer(file);
      reader.onload = () => {
        const binaryData = reader.result as ArrayBuffer;
        resolve(binaryData);
      };
      reader.onerror = (error) => reject(error);
    });
  }, []);

  const handleFileChange = async (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (!file) {
      return;
    }
    const content = await getFileContent(file);
    setContent(content);
  };

  const downloadFile = () => {
    downloadBase64File(encode(content), 'rs.bin', 'application/octet-stream');
  };

  if (isEditMode) {
    return (
      <Form.Group controlId="fileInput" className="mb-3">
        <Form.Label>Upload File</Form.Label>
        <InputGroup>
          <Form.Control
            type="file"
            onChange={handleFileChange}
          />
          <Button
            variant={'outline-secondary'}
            onClick={downloadFile}
            title={'Download'}
          >
            <Download05Icon />
          </Button>
        </InputGroup>
      </Form.Group>
    );
  }

  return (
    <Form.Group controlId="fileInput" className="mb-3">
      <Form.Label>Upload File</Form.Label>
      <Form.Control
        type="file"
        onChange={handleFileChange}
      />
    </Form.Group>
  );
};

export default StaticFileMockContent;
