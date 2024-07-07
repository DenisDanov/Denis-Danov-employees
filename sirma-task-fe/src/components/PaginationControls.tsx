import React from 'react';
import { Button } from '@mui/material';

interface PaginationControlsProps {
    page: number;
    totalPages: number;
    handlePageChange: (newPage: number) => void;
}

const PaginationControls: React.FC<PaginationControlsProps> = ({ page, totalPages, handlePageChange }) => {
    return (
        <div style={{float: 'right', paddingTop: "10px"}}>
            <Button style={{ marginRight: '10px' }}
                    variant="outlined"
                    disabled={page === 0}
                    onClick={() => handlePageChange(page - 1)}
            >
                Prev
            </Button>
            <Button
                variant="outlined"
                disabled={page === totalPages - 1 || page === 1}
                onClick={() => handlePageChange(page + 1)}
            >
                Next
            </Button>
        </div>
    );
};

export default PaginationControls;
