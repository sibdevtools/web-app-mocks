import React, { useState } from 'react';
import ReactDOMServer from 'react-dom/server';
import { Table, Form } from 'react-bootstrap';

export interface TableColumn {
  key: string;
  label: string;
}

export interface StyleProps {
  centerHeaders: boolean,
  textCenterValues: boolean,
}

export interface CustomTableProps {
  columns: TableColumn[];
  data: Array<{ [key: string]: React.ReactNode }>;
  sortableColumns?: string[];
  filterableColumns?: string[];
  styleProps?: StyleProps;
}

const CustomTable: React.FC<CustomTableProps> = ({
                                                   columns,
                                                   data,
                                                   sortableColumns = [],
                                                   filterableColumns = [],
                                                   styleProps = {
                                                     centerHeaders: true,
                                                     textCenterValues: false,
                                                   }
                                                 }) => {
  const [filter, setFilter] = useState<{ [key: string]: string }>({});
  const [sortColumn, setSortColumn] = useState<string>('');
  const [sortDirection, setSortDirection] = useState<'asc' | 'desc'>('asc');

  // Handle filter changes
  const handleFilterChange = (event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>, columnKey: string) => {
    setFilter({ ...filter, [columnKey]: event.target.value });
  };

  // Handle sorting
  const handleSort = (columnKey: string) => {
    if (!sortableColumns.includes(columnKey)) return;
    const direction = sortColumn === columnKey && sortDirection === 'asc' ? 'desc' : 'asc';
    setSortColumn(columnKey);
    setSortDirection(direction);
  };

  // Helper function to extract text from ReactNode
  const extractText = (node: React.ReactNode): string => {
    if (typeof node === 'string' || typeof node === 'number') {
      return node.toString();
    }
    if (!React.isValidElement(node)) {
      return '';
    }
    const htmlString = ReactDOMServer.renderToString(node);
    const div = document.createElement('div');
    div.innerHTML = htmlString;
    return div.innerText || div.textContent || '';
  };

  // Apply filtering
  const filteredData = data.filter((item) => {
    return Object.entries(filter).every(([key, value]) => {
      if (!value) return true; // No filter for this column
      const cellValue = item[key];
      const cellText = extractText(cellValue);
      return cellText.toLowerCase().includes(value.toLowerCase());
    });
  });

  // Apply sorting
  const sortedData = [...filteredData].sort((a, b) => {
    if (!sortColumn) return 0; // No sorting applied
    const aValue = a[sortColumn];
    const bValue = b[sortColumn];

    if (typeof aValue === 'string' && typeof bValue === 'string') {
      return sortDirection === 'asc'
        ? aValue.localeCompare(bValue)
        : bValue.localeCompare(aValue);
    }

    if (typeof aValue === 'number' && typeof bValue === 'number') {
      return sortDirection === 'asc' ? aValue - bValue : bValue - aValue;
    }

    return 0;
  });

  return (
    <Table striped={true} bordered={true} hover={true}>
      <thead className={'table-dark'}>
      <tr className={`${styleProps.centerHeaders ? 'text-center' : ''}`}>
        {columns.map((column) => (
          <th
            key={column.key}
            onClick={() => handleSort(column.key)}
            style={{ cursor: sortableColumns.includes(column.key) ? 'pointer' : 'default' }}
          >
            {column.label}
            {sortableColumns.includes(column.key) && sortColumn === column.key && (
              sortDirection === 'asc' ? ' ▲' : ' ▼'
            )}
          </th>
        ))}
      </tr>
      <tr>
        {columns.map((column) => (
          <th key={`filter-${column.key}`}>
            {filterableColumns.includes(column.key) && (
              <Form.Control
                type={'text'}
                placeholder={`Filter ${column.label}`}
                value={filter[column.key] || ''}
                onChange={(e) => handleFilterChange(e, column.key)}
              />
            )}
          </th>
        ))}
      </tr>
      </thead>
      <tbody>
      {sortedData.map((item, index) => (
        <tr key={index}>
          {columns.map((column) => (
            <td className={`${styleProps.textCenterValues ? 'text-center' : ''}`}
                key={column.key}>{item[column.key]}</td>
          ))}
        </tr>
      ))}
      </tbody>
    </Table>
  );
};

export default CustomTable;
