import React from 'react';
import renderer from 'react-test-renderer';
import About from '../../components/pages/Customer/about/About';

it('component created', () => {
    const component = renderer.create(
        <About />
    )
    const tree = component.toJSON();
    expect(tree).toMatchSnapshot();
})