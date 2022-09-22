import "@testing-library/jest-dom";
import { fireEvent, render, screen } from '@testing-library/react';
import { act } from 'react-dom/test-utils';
import { Provider } from 'react-redux';
import { store } from '../app/store';
import AddSubscription from '../components/pages/AdminSubscriptionPack/AddSubscription';
import { SubRequest } from '../components/pages/AdminSubscriptionPack/models';




// window.ResizeObserver =  
// window.ResizeObserver || jest.mock('use-resize-observer', () => ({
//     __esModule: true,
//     default: jest.fn().mockImplementation(() => ({
//       observe: jest.fn(),
//       unobserve: jest.fn(),
//       disconnect: jest.fn(),
//       global.ResizeObserver = require('resize-observer-polyfill')

//     })),
//   }));

global.ResizeObserver = require('resize-observer-polyfill')


describe('Add Subscription', () => {

  beforeEach(() => {
    jest.clearAllMocks();
  });



  it('Testing Add Subscription Form component', () => {
    jest.setTimeout(50000);
    const onSubmit = jest.fn();
    const setAdd = jest.fn();
    const setOpen = jest.fn();
    const open = true;

    //    onSubmit.mockImplementation(event => {
    //     event.preventDefault();
    //});

    render(
      <Provider store={store}>
        <AddSubscription onSubmit={onSubmit} setAdd={setAdd} open={open} setOpen={setOpen} onadd={() => {
          // 
        }} />
      </Provider>
    );

    //const name = ;
    //const description = ;
    //  const stDate = 

    //const edDate = ;

    //const odd = ;

    //const dd =

    //const cost = 
    //

    const d1 = new Date('Sep 1 2022');
    const d2 = new Date('Sep 3 2022');

    act(() => {
      //console.log('clear error');
      // {name: 'Gold', description: 'Gold Subscription', stDate: Wed Aug 31 2022 00:00:00 GMT+0530 (India Standard Time), edDate: Fri Sep 02 2022 00:00:00 GMT+0530 (India Standard Time), benefits: {…}, …
      console.log('1');
      fireEvent.change(screen.getByRole('textbox', {
        name: /name/i
      }), { target: { value: "Gold" } });
      console.log('2');
      fireEvent.change(screen.getByRole('textbox', {
        name: /description/i
      }), { target: { value: "Gold subscription" } });

      console.log('3');
      fireEvent.change(screen.getByRole('textbox', {
        name: /start date/i
      }), { target: { value: d1 } });
      console.log('4');
      fireEvent.change(screen.getByRole('textbox', {
        name: /period/i
      }), { target: { value: 0 } });


      console.log('5');
      fireEvent.change(screen.getByRole('textbox', {
        name: /end date/i
      }), { target: { value: d2 } });


      console.log('6');
      fireEvent.change(screen.getByRole('radio', {
        name: /enable/i
      }), { target: { value: 'enable' } });

      console.log('7');
      fireEvent.change(screen.getByRole('textbox', {
        name: /delivery discount/i
      }), { target: { value: 0 } });
      console.log('8');
      fireEvent.change(screen.getByRole('textbox', {
        name: /cost/i
      }), { target: { value: 0 } });
      console.log('9');
    })
    //console.log('end of input');



    console.log("start submit button");
    act(() => {
      //console.log("clear second one");
      fireEvent.click(screen.getByRole('button', { name: /submit/i }));
    })
    //console.log("finish");

    const expected: SubRequest = {
      name: "Gold",
      description: "Gold subscription",
      stDate: new Date('Sep 1 2022'),
      edDate: new Date('Sep 3 2022'),
      oneDayDelivery: 'disable',
      deliveryDiscount: 0,
      cost: 0,
      period: 0
    }
    expect(onSubmit).toBeCalledWith(expected);
  });
})