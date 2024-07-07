import React, {useState} from 'react';
import {Button, Typography, Alert, Grid, Paper} from '@mui/material';
import {DataGridPro, GridColDef} from '@mui/x-data-grid-pro';
import axios from 'axios';
import {RowData} from '../types/RowData.ts';
import {EmployeePairDTO} from '../types/EmployeePairData.ts';
import {LicenseInfo} from '@mui/x-license-pro';
import '../css/cells.css';
import PaginationControls from "./PaginationControls.tsx";

LicenseInfo.setLicenseKey('e0d9bb8070ce0054c9d9ecb6e82cb58fTz0wLEU9MzI0NzIxNDQwMDAwMDAsUz1wcmVtaXVtLExNPXBlcnBldHVhbCxLVj0y');

const FileUpload: React.FC = () => {
    const [rows, setRows] = useState<RowData[]>([]);
    const [totalOverlap, setTotalOverlap] = useState<number | null>(null);
    const [employeeIds, setEmployeeIds] = useState<{
        employeeId1: number | null;
        employeeId2: number | null
    }>({employeeId1: null, employeeId2: null});
    const [error, setError] = useState<string | null>(null);
    const [page, setPage] = useState<number>(0);
    const pageSize = 5;

    const handleFileUpload = async (event: React.ChangeEvent<HTMLInputElement>) => {
        const file = event.target.files?.[0];
        if (!file) return;

        const formData = new FormData();
        formData.append('file', file);

        try {
            const response = await axios.post('http://localhost:8080/api/v1/employees/pair', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                },
            });

            const data: EmployeePairDTO = response.data;

            // Create an array to hold rows
            const newRows: RowData[] = [];

            // Iterate over projectsData to extract project ids and days worked
            let count = 0;
            for (const projectId in data.pairDataDTO.projectsData) {
                if (data.pairDataDTO.projectsData.hasOwnProperty(projectId)) {
                    const daysWorked = data.pairDataDTO.projectsData[projectId];
                    newRows.push({
                        id: newRows.length + 1,
                        projectId: +projectId, // Convert projectId to number
                        daysWorked: daysWorked,
                    });
                    count++;
                }
            }

            // Set the employee IDs
            setEmployeeIds({
                employeeId1: data.employee1.empID,
                employeeId2: data.employee2.empID
            });

            // Set the total overlap
            setTotalOverlap(data.pairDataDTO.totalOverlap);

            // Set the rows and calculate total pages
            setRows(newRows);
            setPage(0); // Reset to first page after data update
            setError(null);
        } catch (err: any) {
            const errorMessage = err.response?.data
                ? err.response.data
                : 'An error occurred';
            setEmployeeIds({
                employeeId1: null,
                employeeId2: null
            });
            setError(errorMessage);
            setRows([]);
            setTotalOverlap(null);
        }
    };

    const handleInputChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        event.target.value = '';
    };

    const totalPages = Math.ceil(rows.length / pageSize) == 0 ? 1 : Math.ceil(rows.length / pageSize);

    const handlePageChange = (newPage: number) => {
        setPage(newPage);
    };

    const getCurrentPageRows = (): RowData[] => {
        const startIndex = page * pageSize;
        return rows.slice(startIndex, startIndex + pageSize);
    };

    const CustomHeaderCell = () => (
        <Paper className={"header-custom"} style={{borderBottom: '1px solid #ddd', backgroundColor: "#fbfbfb"}}>
            <Grid container alignItems="center">
                <Grid item xs={6} style={{textAlign: 'center', borderRight: '1px solid #ddd', padding: '5px'}}>
                    <Typography variant="subtitle1" color="textSecondary">
                        Employee ID #1
                    </Typography>
                    <Typography variant="body1">
                        {employeeIds.employeeId1 ?? '-'}
                    </Typography>
                </Grid>
                <Grid item xs={6} style={{textAlign: 'center', padding: '5px'}}>
                    <Typography variant="subtitle1" color="textSecondary">
                        Employee ID #2
                    </Typography>
                    <Typography variant="body1">
                        {employeeIds.employeeId2 ?? '-'}
                    </Typography>
                </Grid>
            </Grid>
        </Paper>
    );

    const CustomFooter = () => (
        <Grid container justifyContent="space-between" alignItems="center"
              style={{padding: '16px', borderTop: '1px solid rgba(224, 224, 224, 1)'}}>
            <Typography variant="body1">
                Total Days Worked: {totalOverlap ?? 0}
            </Typography>
            <Typography variant="body1" style={{marginRight: "19px", textAlign: "center"}}>
                Page {page + 1} of {totalPages}
            </Typography>
        </Grid>
    );

    const columns: GridColDef[] = [
        {
            field: 'projectId',
            headerName: 'Project ID',
            flex: 1,
            headerAlign: 'center',
            align: 'center',
            headerClassName: 'header-cell',
            cellClassName: 'custom-cell',
        },
        {
            field: 'daysWorked',
            headerName: 'Days Worked',
            flex: 1,
            headerAlign: 'center',
            align: 'center',
            headerClassName: 'header-cell',
            cellClassName: 'custom-cell',
        },
    ];

    return (
        <div className={"center-container"}>
            <Typography variant="h4" gutterBottom>
                Upload CSV File
            </Typography>
            <Button variant="contained" component="label">
                Upload CSV
                <input
                    type="file"
                    accept=".csv"
                    hidden
                    onChange={handleFileUpload}
                    // @ts-ignore
                    onClick={handleInputChange}
                />
            </Button>
            {error && <Alert severity="error" style={{marginTop: 20}}>{error}</Alert>}
            <div style={{height: "438px", width: '100%', marginTop: 20}}>
                <DataGridPro
                    rows={getCurrentPageRows()}
                    columns={columns}
                    slots={{
                        toolbar: CustomHeaderCell,
                        footer: CustomFooter
                    }}
                    pagination
                    className="custom-data-grid"
                />
            </div>
            <PaginationControls
                page={page}
                totalPages={totalPages}
                handlePageChange={handlePageChange}
            />
        </div>
    );
};

export default FileUpload;
