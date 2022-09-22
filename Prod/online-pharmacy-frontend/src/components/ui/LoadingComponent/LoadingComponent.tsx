import { Center, Loader } from '@mantine/core'

export default function LoadingComponent() {
    return (
        <Center style={{ height: "300px" }}>
            <Loader data-testid="Loading" />
        </Center >
    )
}
