import React from 'react';
import renderer from 'react-test-renderer';
import { Provider } from 'react-redux';
import { store } from '../app/store';
import App from '../App';

test('renders learn react link', () => {
  const component = renderer.create(
    <Provider store={store}>
      <App />
    </Provider>,
  );

  const tree = component.toJSON();
  expect(tree).toMatchSnapshot();
}
);
