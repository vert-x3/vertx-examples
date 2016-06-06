import React from 'react';
import AltContainer from 'alt/AltContainer';

import VertxStore from '../stores/VertxStore.js';
import VertxActions from '../actions/VertxActions.js';

import VertxWidget from './VertxWidget.jsx';

export default class App extends React.Component {

  render()
  {
    return (
        <div className="">
            <VertxWidget />
        </div>
    );
  }

}
