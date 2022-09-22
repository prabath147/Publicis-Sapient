import { Button, Container, Group, Image } from '@mantine/core';
import { Link } from 'react-router-dom';
import Images from '../../../../../src/images/404-error-image.jpg';


export default function PageNotFound() {
  return (
    <Container>
      <Image className="animate__animated animate__tada" style={{ marginTop: 100, width: 350, marginLeft: 'auto', marginRight: 'auto' }}
        src={Images}
      />
      <Group position="center">
        <Link to="/">
          <Button variant="outline" size="sm">
            Take me back to home page
          </Button>
        </Link>
      </Group>
    </Container>
  );
}