import { render } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import AdminDash from '../../components/pages/Admin/adminDash/adminDash';
import mockAxios from '../../__mocks__/axios';

it('Testing Manager Dashboard component', () => {
    mockAxios.get.mockImplementationOnce(() => Promise.resolve({data: 10}));
    mockAxios.get.mockImplementationOnce(() => Promise.resolve({data: {totalRecords:100}}));
    mockAxios.get.mockImplementationOnce(() => Promise.resolve({data: {totalRecords:200}}));
    mockAxios.get.mockImplementationOnce(() => Promise.resolve({data: 20}));
    mockAxios.get.mockImplementationOnce(() => Promise.resolve({data: 30}));

    render(
        <BrowserRouter>
            <AdminDash />
        </BrowserRouter>
    );

    
    expect(screen).toMatchSnapshot();
});

