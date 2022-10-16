import React from "react";
import ReactDOM from "react-dom/client";
import { BrowserRouter, Routes, Route, useNavigate } from "react-router-dom";
import Layout from "./pages/Layout";
import Home from "./pages/Home";
import Login from "./pages/Login";
import Register from "./pages/Register";
import Generation from "./pages/Generation";
import NoPage from "./pages/NoPage";

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Layout />}>
          <Route index element={<Home />} />
          <Route path="login" element={<Login />} />
          <Route path="register" element={<Register />} />
          <Route path="generation" element={<Generation />} />
          <Route path="*" element={<NoPage />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}

const root = ReactDOM.createRoot(document.getElementById('root'));


function initTabs (tabsElt, contentElt) {
  // Select some elements
  var tabElts = tabsElt.querySelectorAll('.centering-tabs__tab');
  var contentElts = contentElt.querySelectorAll('.centering-tabs__content__item');
  // For each tab and content container ...
  for (var i = 0; i < tabElts.length; i++) {
    var currentTab = tabElts[i];
    var currentContent = contentElts[i];
    // Add a data-index attribute
    currentTab.setAttribute('data-index', i);
    currentContent.setAttribute('data-index', i);
    // If it is the first tab ...
    if (i === 0) {
      // Add isActive classes
      currentTab.classList.add('centering-tabs__tab--isActive');
      currentContent.classList.add('centering-tabs__content__item--isActive');
    }
    // Add a click handler to tabs
    currentTab.addEventListener('click', handleSelect);
  }
  function handleSelect (e) {
    var tabElt = e.target;
    var indexAsStr = tabElt.getAttribute('data-index');
    var isActive = !!tabElt.classList.contains('centering-tabs__tab--isActive');
    // If selected tab is active ...
    if (!isActive) {
      // For each tab and content container ...
      for (var i = 0; i < tabElts.length; i++) {
        var currentTabElt = tabElts[i];
        var currentTabContentElt = contentElts[i];
        // Deactivate all of them except for the one user just selected
        if (currentTabElt.getAttribute('data-index') !== indexAsStr) {
          currentTabElt.classList.remove('centering-tabs__tab--isActive');
          currentTabContentElt.classList.remove('centering-tabs__content__item--isActive');
        } else {
          // Activate selected tab
          currentTabElt.classList.add('centering-tabs__tab--isActive');
          currentTabContentElt.classList.add('centering-tabs__content__item--isActive');

          var isCodexElt = [
            'text-to-sql',
            'text-to-api',
            'code-continuation',
          ].includes(currentTabContentElt.id);

          // We (conditionally) are about to change this, and we need this orig value as a reference
          // var currentTabContentEltHeightProp = currentTabContentElt.style.height;

          // If it is a Codex code example ...
          if (isCodexElt) {
            // Hide the response
            var spans = getCodexResponse(currentTabContentElt.id);

            if (spans) {
              forEach(function (current) {
                current.classList.add('typing');
              }, spans);

              // Fix the box height
              var rectHeight = currentTabContentElt.getBoundingClientRect().height;
              currentTabContentElt.style.height = rectHeight + 'px';
            }
          }

          var typingElts = currentTabContentElt.querySelectorAll('.typing');
          // If there are .typing elements ...
          if (typingElts && typingElts.length) {
            // Make them all invisible
            forEach(function (current) {
              current.classList.add('invisible');
            }, typingElts)
            // After 500ms...
            setTimeout(function () {
              // Type them in sequential order (not at the same time)
              sequence(function (current) {
                current.classList.remove('invisible');
                return typing(current, 6);
              }, typingElts).then(function () {
                // All done typing. Restore the box height to what it was
                // currentTabContentElt.style.height = currentTabContentEltHeightProp;
                // We don't actually need to do this, because as soon as you enter the setTimeout callback, the height is empty. Shrug
              });
            }, 500);
          }
          
          // Shift the tab row so the selected one is centered.
          var tabsLeft = tabsElt.getBoundingClientRect().x;
          var tabsWidth = tabsElt.offsetWidth;
          var tabsCenter = tabsLeft + tabsWidth / 2;
          var tabLeft = tabElt.getBoundingClientRect().x;
          var tabWidth = tabElt.offsetWidth;
          var tabCenter = tabLeft + tabWidth / 2;
          var translateDelta = (tabCenter - tabsCenter) * -1;
          var tabsInnerElt = tabsElt.querySelector('.centering-tabs__inner');
          var currentTranslateVal = getTranslateX(tabsInnerElt);
          var nextTranslateVal = currentTranslateVal + translateDelta;
          var minTransformVal = (tabsInnerElt.scrollWidth - tabsWidth) * -1;

          if (nextTranslateVal > 0) {
            tabsInnerElt.style.transform = 'translateX(0)';
          } else if (nextTranslateVal < minTransformVal) {
            tabsInnerElt.style.transform = 'translateX(' + minTransformVal + 'px)';
          } else {
            tabsInnerElt.style.transform = 'translateX(' + nextTranslateVal + 'px)';
          }
        }
      }
    }
  }
  function getTranslateX (elt) {
    var style = window.getComputedStyle(elt);
    var matrix = new DOMMatrix(style.transform);
    return matrix.m41;
  }
}

function initAllTabs () {
  var tabRows = document.querySelectorAll('.centering-tabs');
  var tabContentWrappers = document.querySelectorAll('.centering-tabs__content');

  for (var i = 0; i < tabRows.length; i++) {
    initTabs(tabRows[i], tabContentWrappers[i]);
  }
}

function typing (elt, speed) {
  return new Promise(function (resolve) {
    var text = elt.innerHTML.replace(/<br>/g, '\n').split('');
    var heightProp = elt.style.height;
    var rectHeight = elt.getBoundingClientRect().height;
    elt.style.height = rectHeight + 'px';
    elt.innerHTML = '';
  
    function addChar () {
      var char = text.shift();
  
      if (char === '\n') {
        char = '<br>';
      }
  
      elt.innerHTML = elt.innerHTML + char;
  
      if (text.length) {
        setTimeout(function () {
          addChar();
        }, speed);
      } else {
        elt.style.height = heightProp;
        resolve();
      }
    }
    
    addChar();
  });
}

// We need to animate the part of the code example that is the Codex's response
// This function gets those parts in a list
function getCodexResponse (id) {
  var container = document.getElementById(id);

  switch (id) {
    case 'text-to-sql':
      return container.querySelectorAll('span.string:not(.triple-quoted-string)');
    case 'text-to-api':
      return container.querySelectorAll('span:not(.triple-quoted-string)');
    case 'code-continuation':
      return container.querySelectorAll('span:not(.triple-quoted-string)');
  }
}

function initExpandableCodeBlocks () {
  function initExpandableCodeBlock (elt) {
    var toggleBtnElt = elt.querySelector('.code-block--expandable__toggle');
    toggleBtnElt.addEventListener('click', function () {
      var isExpanding = !elt.classList.contains('code-block--expandable--expanded');
      elt.classList.toggle('code-block--expandable--expanded');
      if (isExpanding) {
        // Get inner divs
        var divs = elt.querySelectorAll('div');
        // Make them all invisible
        forEach(function (current) {
          current.classList.add('invisible');
        }, divs)
        setTimeout(function () {
          // Show them in sequential order (not at the same time)
          sequence(function (current) {
            return new Promise(function (resolve) {
              setTimeout(function () {
                current.classList.remove('invisible');
                resolve();
              }, 50)
            })
          }, divs)
        }, 250);
      }
    });
  }
  
  setTimeout(function () {
    forEachElt('.code-block--expandable', initExpandableCodeBlock);
  }, 0);
}

// For the code examples on the API page and Microsoft for Startups page, wrap text nodes with spans
function initAPIPageCodeExamples () {
  var isAPIPage = !!document.querySelector('.homepage') || !!document.querySelector('.page-microsoft-for-startups');
  if (!isAPIPage) return;
  var codeElts = document.querySelectorAll('code');
  forEach(function (codeElt) {
    var textNodes = filterTextNodes(codeElt.childNodes);
    forEach(function (current) {
      wrapNodeWithElt(current, 'span');
    }, textNodes);
  }, codeElts)
}

function initNavToggle () {
  var navToggleElt = document.querySelector('.api-secondary-nav__toggle');
  var navContainerElt = document.querySelector('.api-secondary-nav');

  if (!navToggleElt || !navContainerElt) {
    return
  }

  navToggleElt.addEventListener('click', function () {
    navContainerElt.classList.toggle('api-secondary-nav--open');
  })
}

// Utility

// Iterate over lists (not just arrays)
function forEach (callback, data) {
  for (var i = 0; i < data.length; i++) {
    var current = data[i];
    callback(current, i);
  }
}

// For each element that matches `selector` ...
function forEachElt (selector, callback, parent) {
  var elts = (parent || document).querySelectorAll(selector);

  forEach(callback, elts);
}

// Wrap a node with an element
function wrapNodeWithElt (node, tagName) {
  var elt = document.createElement(tagName);
  node.after(elt);
  elt.appendChild(node);
  return elt;
}

// Given a list, return a list of only Text nodes
function filterTextNodes (data) {
  var textNodes = [];
  forEach(function (current) {
    if (current.nodeName === '#text') {
      textNodes.push(current);
    }
  }, data);
  return textNodes;
}

// Perform a function on each item in a list sequentially. The function must be a promise.
// Also, this function returns a promise that resolves when the whole sequence is done
function sequence (asyncFn, data) {
  return new Promise(function (resolve) {
    var promise = Promise.resolve();

    forEach(function (current) {
      promise = promise.then(function () {
        return asyncFn(current);
      });
    }, data);

    promise.then(resolve);
  });
}

document.addEventListener('DOMContentLoaded', function () {
  initExpandableCodeBlocks();
  initNavToggle();
  initAPIPageCodeExamples();
  initAllTabs();
});

root.render(<App />);