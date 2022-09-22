import { Container } from '@mantine/core'
import { Link } from 'react-router-dom'

export default function NotAllowed() {
    return (
        <Container>

            <br />
            <br />
            <h1>
                Not Allowed
            </h1>
            <br />
            <h3>
                <Link to="/">
                    Go to Home
                </Link>
            </h3>
        </Container>
    )
}
