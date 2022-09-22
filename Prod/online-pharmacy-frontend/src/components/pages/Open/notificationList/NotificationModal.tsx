import { Grid, Modal, Textarea, TextInput } from "@mantine/core";
import { DatePicker } from "@mantine/dates";

export default function NotificationModal(props) {

    return (
        <>
            <Modal
                opened={props.opened}
                onClose={() => props.setOpened(false)}
                title="Notification Details"
            >
                <form>
                    <Grid grow>
                        <Grid.Col span={5}>
                            <TextInput label="Notification ID" value={props.row.id} readOnly={true} />
                        </Grid.Col>
                        <Grid.Col span={5}>
                            <TextInput label="Severity" value={props.row.severity} readOnly={true} />
                        </Grid.Col>
                    </Grid>
                    <Grid>
                        <Grid.Col>
                            <TextInput label="Message" value={props.row.message} readOnly={true} />
                        </Grid.Col>
                    </Grid>
                    <Grid>
                        <Grid.Col>
                            <Textarea autosize={true} label="Description" value={props.row.description} readOnly={true} />
                        </Grid.Col>
                    </Grid>
                    <Grid>
                        <Grid.Col span={5}>
                            <TextInput label="Status" value={props.row.status} readOnly={true} />
                        </Grid.Col>
                        <Grid.Col span={7}>
                            <DatePicker value={new Date(props.row.createdOn)} label="Created On" readOnly={true} />
                        </Grid.Col>
                    </Grid>

                </form>

            </Modal>
        </>
    )
}