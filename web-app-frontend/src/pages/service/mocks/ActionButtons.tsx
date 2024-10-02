import React from 'react';
import { Button, ButtonGroup, OverlayTrigger, Tooltip } from 'react-bootstrap';
import { Copy01Icon, Delete01Icon, PencilEdit01Icon } from 'hugeicons-react';
import { Mock } from '../../../api/service';


export interface ActionButtonsProps {
  mock: Mock;
  onEdit: () => void;
  onCopy: () => void;
  onDelete: () => void;
  showTooltip: { [key: number]: boolean };
}

export const ActionButtons: React.FC<ActionButtonsProps> = ({
                                                              mock,
                                                              onEdit,
                                                              onCopy,
                                                              onDelete,
                                                              showTooltip
                                                            }) => (
  <ButtonGroup>
    <Button variant="primary" onClick={onEdit}>
      <PencilEdit01Icon />
    </Button>
    <OverlayTrigger show={mock.mockId in showTooltip && showTooltip[mock.mockId]} placement="top"
                    overlay={<Tooltip id={`tooltip-${mock.mockId}`}>Copied!</Tooltip>}>
      <Button variant="info" onClick={onCopy}>
        <Copy01Icon />
      </Button>
    </OverlayTrigger>
    <Button variant="danger" onClick={onDelete}>
      <Delete01Icon />
    </Button>
  </ButtonGroup>
);
