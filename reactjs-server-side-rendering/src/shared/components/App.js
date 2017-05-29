import React from 'react'
import Post from './Post'
import {Link, IndexLink} from 'react-router'

const allPostsUrl = '/api/post';

class App extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      posts: props.posts || []
    }
  }

  componentDidMount() {
    const request = new XMLHttpRequest();
    request.open('GET', allPostsUrl, true);
    request.setRequestHeader('Content-type', 'application/json');

    request.onload = () => {
      if (request.status === 200) {
        this.setState({
          posts: JSON.parse(request.response)
        });
      }
    };

    request.send()
  }

  render() {
    const posts = this.state.posts.map((post) => {
      const linkTo = `/${post.id}/${post.slug}`;

      return (
        <li key={post.id}>
          <Link to={linkTo}>{post.title}</Link>
        </li>
      )
    });

    const {postId, postName} = this.props.params;
    let postTitle, postContent;
    if (postId && postName) {
      const post = this.state.posts.filter(p => p.id == postId)[0];
      if (post) {
        postTitle = post.title;
        postContent = post.content;
      }
    }

    return (
      <div>
        <IndexLink to="/">Home</IndexLink>
        <h3>Posts</h3>
        <ul>
          {posts}
        </ul>

        {postTitle && postContent ? (
          <Post title={postTitle} content={postContent}/>
        ) : (
          this.props.children
        )}
      </div>
    )
  }
}

export default App
