import React, { useCallback } from 'react';
import { Form } from 'react-bootstrap';

export interface StaticMockBinaryContentProps {
  setContent: (content: ArrayBuffer) => void;
}

const StaticFileMockContent: React.FC<StaticMockBinaryContentProps> = ({ setContent }) => {
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
