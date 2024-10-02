import React, { useEffect, useState } from 'react';
import { createMock, getMock, updateMock } from '../../api/service';
import { useNavigate, useParams } from 'react-router-dom';
import { contextPath, Method, methods, MockType } from '../../const/common.const';
import { decodeBase64ToText, encodeTextToBase64 } from '../../utils/base.64converters';
import MockForm from './MockForm';
import { Loader } from '../../components/Loader';

const AddEditMockPage: React.FC = () => {
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();
  const { serviceId, mockId } = useParams();

  const [mockName, setMockName] = useState('');
  const [method, setMethod] = useState<Method>(methods[0]);
  const [path, setPath] = useState('');
  const [delay, setDelay] = useState<number>(0);
  const [mockType, setMockType] = useState<MockType>('STATIC');
  const [meta, setMeta] = useState<{ [key: string]: string }>({ STATUS_CODE: '200' });
  const [content, setContent] = useState(new ArrayBuffer(0));

  useEffect(() => {
    if (mockId) {
      fetchMock();
    } else {
      setLoading(false);
    }
  }, [mockId]);

  const fetchMock = async () => {
    setLoading(true);
    if (!(serviceId && mockId)) {
      setLoading(false);
      return;
    }
    try {
      const response = await getMock(+serviceId, +mockId);
      const body = response.data.body;
      setMockName(body.name);
      setMethod(body.method);
      setPath(body.path);
      setDelay(body.delay);
      setMockType(body.type);
      setMeta(body.meta);
      setContent(decodeBase64ToText(body.content));
    } catch (error) {
      console.error('Failed to fetch mock:', error);
    } finally {
      setLoading(false);
    }
  };

  if (!serviceId) {
    navigate(contextPath);
    return;
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const mockData = {
        name: mockName,
        method,
        path,
        type: mockType,
        delay: delay,
        meta,
        content: encodeTextToBase64(content)
      };
      if (mockId) {
        await updateMock(+serviceId, +mockId, mockData);
      } else {
        await createMock(+serviceId, mockData);
      }
      navigate(`${contextPath}service/${serviceId}/mocks`);
    } catch (error) {
      console.error('Failed to submit mock:', error);
    }
  };

  const navigateBack = () => {
    navigate(`${contextPath}service/${serviceId}/mocks`);
  };

  return (
    <MockForm
      loading={loading}
      mockName={mockName}
      method={method}
      path={path}
      delay={delay}
      mockType={mockType}
      meta={meta}
      content={content}
      setMockName={setMockName}
      setMethod={setMethod}
      setPath={setPath}
      setDelay={setDelay}
      setMockType={setMockType}
      setMeta={setMeta}
      setContent={setContent}
      onSubmit={handleSubmit}
      isEditMode={!!mockId}
      navigateBack={navigateBack}
    />
  );
};

export default AddEditMockPage;
