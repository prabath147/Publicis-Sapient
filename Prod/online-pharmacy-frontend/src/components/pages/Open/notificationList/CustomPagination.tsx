import { MantineProvider, Pagination, Select } from "@mantine/core";

export function CustomPagination(props) {
    const textStyle = {
        padding: '10px'
    }

    return (
        <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
            <MantineProvider withGlobalStyles withNormalizeCSS>
                <Pagination page={props.activePage} onChange={props.setPage} total={props.totalPages} style={{ margin: '10px', padding: '10px' }} />
                <div style={{ display: 'flex', alignItems: 'center' }}>
                    <div style={{ display: 'flex', alignItems: 'center', margin:'10px' }}>
                        <p style={textStyle}>Records: </p>
                        <Select data={['10','20','50']} style={{ maxWidth: 70 }} value={props.itemsPerPage.toString()} onChange={(e: string) => {props.setItemsPerPage(parseInt(e)); props.setPage(1)}} />
                    </div>
                    <div style={{ display: 'flex', alignItems: 'center', margin:'10px' }}>
                        <p style={textStyle}>Page: </p>
                        <Select data={Array.from({ length: props.totalPages }, (_, k) => (k + 1).toString())} style={{ maxWidth: 70 }} value={props.activePage.toString()} onChange={(e: string) => props.setPage(parseInt(e))} />
                        <p style={textStyle}>of {props.totalPages}</p>
                    </div>
                </div>
            </MantineProvider>
        </div>
    );
}