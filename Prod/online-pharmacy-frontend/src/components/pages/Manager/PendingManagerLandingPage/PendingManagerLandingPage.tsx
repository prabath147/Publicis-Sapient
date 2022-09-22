import { Title } from "@mantine/core"
import UserBadge from '../../../ui/UserBadge/UserBadge'

export default function PendingManagerLandingPage() {
    return (
        <div style={{
            display: 'flex', flexDirection: 'column', alignItems: 'center', height: '100%', justifyContent: 'center'
        }}><br />
            <br /><br />
            <br />
            < Title > Your Application is Still under Processing</Title>
            <br />
            <br />
            Please Retry Later
            <br />
            <br />
            <UserBadge />
        </ div>
    )
}
