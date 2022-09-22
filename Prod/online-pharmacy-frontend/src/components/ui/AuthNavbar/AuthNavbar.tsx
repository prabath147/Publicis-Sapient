import { Container, Image } from '@mantine/core';
import { Link } from 'react-router-dom';
import logo from './../../../images/logo.jpg';


export default function AuthNavbar() {
    return (
        <Container>
            <div className="main-nav">
                <Link to="/">
                    <Image src={logo} />
                </Link>
            </div>
        </Container>
    )
}
