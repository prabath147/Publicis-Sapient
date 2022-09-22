import { useForm } from '@mantine/form';
import { fireEvent, render, screen } from '@testing-library/react';
import renderer from 'react-test-renderer';
import AddressForm, { addressInitialValues, AddressInterface, addressValidation } from '../../components/ui/forms/AddressForm';

const MockAddressForm = () => {
    const form = useForm<AddressInterface>({
        initialValues: addressInitialValues(),
        validate: addressValidation()
    })
    return (
        <div>
            <AddressForm form={form} />
            <button onClick={() => {
                form.isValid()
            }}>place order</button>
        </div>
    )
}

describe('AddressForm unit tests', () => {

    it('component created', () => {
        const component = renderer.create(<MockAddressForm />)
        const tree = component.toJSON();
        expect(tree).toMatchSnapshot();
    })


    it('test validations', async () => {
        const { asFragment } = render(<MockAddressForm />)

        expect(screen.getByRole('textbox', { name: /Street/i })).toBeInTheDocument();
        fireEvent.click(screen.getByText(/place order/i))

        expect(asFragment()).toMatchSnapshot();
    })

})