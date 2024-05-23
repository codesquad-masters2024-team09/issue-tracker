import axios from "axios"

const send = async ({method='', path='', data={}} = {}) => {
	const commonUrl = 'http://localhost:8080'
  const url = commonUrl + path

  const headers = {
    // "Access-Control-Allow-Origin":commonUrl,
    "Access-Control-Allow-Origin": "*", // 쿠키가 없을 경우는 *
    "Access-Control-Allow-Credentials": true,
    "content-type": "application/json;charset=UTF-8",
    "accept": "application/json,",
    "SameSite": "None",
  };

  const options = {
    method,
    url,
    headers,
    data,
  }

  try {
    const response = await axios(options);
    return response.data
  }
  catch(error) {
    throw error
  }
}

const getApi = ({path=''} = {}) => {
  return send({method: 'GET', path})
}

const putApi = ({path='', data={}} = {}) => {
  return send({method: 'PUT', path, data})
}

const patchApi = ({path='', data={}} = {}) => {
  return send({method: 'PATCH', path, data})
}

const postApi = ({path='', data={}} = {}) => {
  return send({method: 'POST', path, data})
}

const delApi = ({path='', data={}} = {}) => {
  return send({method:'DELETE', path, data})
}

export {
  getApi,
  putApi,
  patchApi,
  postApi,
  delApi,
}