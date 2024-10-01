import React from 'react';

export const Loader = () => {
  return (
    <div className={'container h-100 w-100 d-flex justify-content-center'}>
      <div className="spinner-border" role="status">
        <span className="visually-hidden">Loading...</span>
      </div>
    </div>
  );
}
