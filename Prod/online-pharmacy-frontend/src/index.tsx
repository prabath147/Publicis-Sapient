/** @format */

import { MantineProvider } from '@mantine/core';
import { ModalsProvider } from '@mantine/modals';
import { NotificationsProvider } from '@mantine/notifications';
import { createRoot } from 'react-dom/client';
import { Provider } from 'react-redux';
import App from './App';
import './App.css';
import { store } from './app/store';

// eslint-disable-next-line @typescript-eslint/no-non-null-assertion
const container: HTMLElement = document.getElementById('root')!;
const root = createRoot(container);

root.render(
  <>
    <MantineProvider>
      <ModalsProvider>
        <NotificationsProvider position='top-right'>
          <Provider store={store}>
            <App />
          </Provider>
        </NotificationsProvider>
      </ModalsProvider>
    </MantineProvider>
  </>,
);
