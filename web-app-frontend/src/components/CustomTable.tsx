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

export interface ReactCell {
  /**
   * Data UI representation
   */
  representation: React.ReactNode;
  /**
   * Data text representation for filtering and sorting
   */
  value?: string;
}

export type Cell = ReactCell | string;

export interface CustomTableProps {
  columns: TableColumn[];
  data: Array<{ [key: string]: Cell }>;
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

  const getCellValue = (cell: Cell): string => {
    if (typeof cell === 'string') {
      return cell;
    }
    return cell.value || '';
  };

  const getCellRepresentation = (cell: Cell): React.ReactNode => {
    if (typeof cell === 'string') {
      return (cell);
    }
    return cell.representation;
  };

  // Apply filtering
  const filteredData = data.filter((item) => {
    return Object.entries(filter).every(([key, value]) => {
      if (!value) return true; // No filter for this column
      const cell = item[key];
      let cellValue = getCellValue(cell);
      return cellValue.toLowerCase().includes(value.toLowerCase());
    });
  });

  // Apply sorting
  const sortedData = [...filteredData].sort((a, b) => {
    if (!sortColumn) return 0; // No sorting applied
    const aValue = getCellValue(a[sortColumn]);
    const bValue = getCellValue(b[sortColumn]);

    return sortDirection === 'asc'
      ? aValue.localeCompare(bValue)
      : bValue.localeCompare(aValue);
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
                key={column.key}>{getCellRepresentation(item[column.key])}</td>
          ))}
        </tr>
      ))}
      </tbody>
    </Table>
  );
};

export default CustomTable;
