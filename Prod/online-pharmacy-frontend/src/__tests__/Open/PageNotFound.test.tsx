import { BrowserRouter } from 'react-router-dom';
import renderer from 'react-test-renderer';
import PageNotFound from '../../components/pages/Open/PageNotFound/PageNotFound';

it('component created', () => {
    const component = renderer.create(
        <BrowserRouter>
            <PageNotFound />
        </BrowserRouter>
    )
    const tree = component.toJSON();
    expect(tree).toMatchSnapshot();
})
