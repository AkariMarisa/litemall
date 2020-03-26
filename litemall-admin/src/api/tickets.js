import request from '@/utils/request'

export function listTicket(query) {
  return request({
    url: '/tickets/list',
    method: 'get',
    params: query
  })
}

export function useTicket(data) {
  return request({
    url: '/tickets/use',
    method: 'post',
    data
  })
}
