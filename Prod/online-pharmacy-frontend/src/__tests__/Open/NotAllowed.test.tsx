import React from 'react';
import { BrowserRouter } from 'react-router-dom';
import renderer from 'react-test-renderer';
import NotAllowed from '../../components/pages/Open/NotAllowed/NotAllowed';

it('component created', () => {
    const component = renderer.create(
        <BrowserRouter>
            <NotAllowed />
        </BrowserRouter>
    )
    const tree = component.toJSON();
    expect(tree).toMatchSnapshot();
})
