import React, { useCallback } from 'react';

export interface StaticMockBinaryContentProps {
  setContent: (content: ArrayBuffer) => void,
}

const StaticFileMockContent: React.FC<StaticMockBinaryContentProps> = ({
                                                                         setContent,
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

  return (
    <div className={'mb-3'}>
      <label htmlFor={`fileInput`} className="form-label">Upload File</label>
      <input
        type="file"
        id="fileInput"
        className="form-control"
        aria-describedby="fileInputFeedback"
        onChange={handleFileChange}
      />
    </div>
  );
};

export default StaticFileMockContent;
