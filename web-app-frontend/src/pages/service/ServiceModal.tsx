import { Button, Form, Modal } from 'react-bootstrap';
import { Cancel01Icon, FloppyDiskIcon } from 'hugeicons-react';
import React from 'react';

export interface ServiceModal {
  showModal: boolean;
  setShowModal: (showModal: boolean) => void;
  modalMode: 'add' | 'edit';
  newServiceCode: string;
  setNewServiceCode: (code: string) => void;
  handleSave: () => void;
}

export const ServiceModal: React.FC<ServiceModal> = ({
                                                       showModal,
                                                       setShowModal,
                                                       modalMode,
                                                       newServiceCode,
                                                       setNewServiceCode,
                                                       handleSave
                                                     }) => {
  return (
    <Modal show={showModal} onHide={() => setShowModal(false)}>
      <Modal.Header closeButton>
        <Modal.Title>{modalMode === 'edit' ? 'Edit Service' : 'Add New Service'}</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <Form>
          <Form.Group controlId="serviceCode">
            <Form.Label>Service Code</Form.Label>
            <Form.Control
              type="text"
              value={newServiceCode}
              onChange={(e) => setNewServiceCode(e.target.value)}
            />
          </Form.Group>
        </Form>
      </Modal.Body>
      <Modal.Footer>
        <Button
          variant="secondary"
          onClick={() => setShowModal(false)}
          title={'Cancel'}
        >
          <Cancel01Icon />
        </Button>
        <Button
          variant="primary"
          onClick={handleSave}
          title={'Save'}
        >
          <FloppyDiskIcon />
        </Button>
      </Modal.Footer>
    </Modal>
  );
};
